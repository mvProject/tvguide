package com.mvproject.tvprogramguide.data.datasource

import android.util.Xml
import com.fleeksoft.ksoup.Ksoup
import com.mvproject.tvprogramguide.data.model.parse.ChannelParseModel
import com.mvproject.tvprogramguide.data.model.parse.ProgramParseModel
import com.mvproject.tvprogramguide.domain.contract.IProgramDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray
import org.xmlpull.v1.XmlPullParser
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream

class ProgramDataSource(
    private val client: HttpClient,
) : IProgramDataSource {
    override suspend fun downloadAndParseXml(
        url: String,
        onProgrammeParsed: suspend (ProgramParseModel) -> Unit
    ) = withContext(Dispatchers.IO) {
        var tempFile: File? = null
        try {
            tempFile = File.createTempFile("programme", ".xml.gz")
            client.prepareGet(url).execute { response ->
                val channel = response.bodyAsChannel()
                FileOutputStream(tempFile).use { out ->
                    while (!channel.isClosedForRead) {
                        val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                        while (!packet.exhausted()) {
                            val bytes = packet.readByteArray()
                            out.write(bytes)
                        }
                    }
                }
            }
            parseXmlFromInputStream(tempFile.inputStream(), onProgrammeParsed)
        } catch (ex: Exception) {
            Timber.e("Error downloading or parsing XML: ${ex.message}")
            throw ex
        } finally {
            tempFile?.delete()
        }
    }

    override suspend fun downloadAndParseChannels(
        url: String,
        onChannelParsed: suspend (ChannelParseModel) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val html = client.get(url).bodyAsText()
            val document = Ksoup.parse(html)
            val rows = document.select("tr").drop(1)
            rows.forEach { row ->
                val cells = row.select("td")
                if (cells.size >= 3) {
                    val id = cells[2].text()
                    val logo = cells[0].select("img").attr("src")
                    val names = cells[1].textNodes().map { it.text() }
                    names.forEach { name ->
                        if (name.isNotBlank()) {
                            onChannelParsed(ChannelParseModel(id = id, name = name, logo = logo))
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            Timber.e("Error downloading channels: ${ex.message}")
        }
    }

    private suspend fun parseXmlFromInputStream(
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
                    ensureActive()
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (parser.name) {
                                "programme" -> {
                                    currentProgramme = ProgramParseModel(
                                        start = parser.getAttributeValue(null, "start") ?: "",
                                        stop = parser.getAttributeValue(null, "stop") ?: "",
                                        channel = parser.getAttributeValue(null, "channel") ?: ""
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
                                currentProgramme?.let { prog ->
                                    if (prog.start.isNotBlank() && prog.stop.isNotBlank() && prog.channel.isNotBlank()) {
                                        onProgrammeParsed(prog)
                                    }
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
