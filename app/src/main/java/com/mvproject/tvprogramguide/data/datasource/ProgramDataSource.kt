package com.mvproject.tvprogramguide.data.datasource

import android.util.Xml
import com.mvproject.tvprogramguide.data.model.parse.ProgramParseModel
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import timber.log.Timber
import java.io.BufferedInputStream
import java.util.zip.GZIPInputStream

class ProgramDataSource(
    private val client: HttpClient,
) {
    suspend fun downloadAndParseXml(
        url: String,
        onProgrammeParsed: suspend (ProgramParseModel) -> Unit
    ) = withContext(Dispatchers.IO) {

        try {
            client.use { service ->
                service.prepareGet(url).execute { response ->
                    Timber.i("testing File download started. Content length: ${response.contentLength()}")
                    val channel = response.bodyAsChannel()
                    parseGzippedXml(channel, onProgrammeParsed)
                }
            }
        } catch (ex: Exception) {
            client.close()
            Timber.e("testing Error downloading or parsing XML: ${ex.message}")
        }
    }

    private suspend fun parseGzippedXml(
        channel: ByteReadChannel,
        onProgrammeParsed: suspend (ProgramParseModel) -> Unit
    ) = withContext(Dispatchers.Default) {
        val inputStream = channel.toInputStream()
        BufferedInputStream(inputStream).use { bufferedInput ->
            GZIPInputStream(bufferedInput).use { gzipInput ->
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(gzipInput, null)

                var eventType = parser.eventType
                var currentProgramme: ProgramParseModel? = null
                var currentTag: String? = null

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (parser.name) {
                                "programme" -> {
                                    currentProgramme = ProgramParseModel(
                                        start = parser.getAttributeValue(null, "start"),
                                        stop = parser.getAttributeValue(null, "stop"),
                                        channel = parser.getAttributeValue(null, "channel")
                                    )
                                }

                                else -> currentTag = parser.name
                            }
                        }

                        XmlPullParser.TEXT -> {
                            currentProgramme?.let { programme ->
                                when (currentTag) {
                                    "title" -> programme.title = parser.text
                                    "desc" -> programme.desc = parser.text
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> {
                            if (parser.name == "programme") {
                                currentProgramme?.let {
                                    onProgrammeParsed(it)
                                }
                                currentProgramme = null
                            }
                            currentTag = null
                        }
                    }
                    eventType = parser.next()
                }
            }
        }

    }
}