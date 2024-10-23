package com.mvproject.tvprogramguide.data.datasource

import android.util.Xml
import com.mvproject.tvprogramguide.data.model.parse.ProgramParseModel
import com.mvproject.tvprogramguide.data.network.EpgService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream
import javax.inject.Inject

class ProgramDataSource @Inject
constructor(
    private val service: EpgService,
) {
    suspend fun downloadAndParseXml(
        url: String,
        onProgrammeParsed: suspend (ProgramParseModel) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val response = service.downloadFile(url)
            Timber.d("testing XMLParser File download started. Content length: ${response.contentLength()}")
            response.byteStream().use { inputStream ->
                parseGzippedXml(inputStream, onProgrammeParsed)
            }
        } catch (ex: Exception) {
            Timber.e("testing XMLParser Error downloading or parsing XML: ${ex.message}")
            throw ex
        }
    }

    private suspend fun parseGzippedXml(
        inputStream: InputStream,
        onProgrammeParsed: suspend (ProgramParseModel) -> Unit
    ) = withContext(Dispatchers.Default) {
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