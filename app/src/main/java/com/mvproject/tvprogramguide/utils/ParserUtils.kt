package com.mvproject.tvprogramguide.utils

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import timber.log.Timber

object ParserUtils {
    private const val TABLE_TAG = "tr"
    private const val TABLE_ROW_TAG = "td"
    private const val IMAGE_QUERY_TAG = "img"
    private const val IMAGE_SRC_TAG = "src"

    suspend fun loadElements(sourceUrl: String): List<Element> {
        Timber.w("testing loadElements from $sourceUrl")
        val parsedDocument: Document = Ksoup.parseGetRequest(url = sourceUrl)
        val parsedTable = parsedDocument.select(TABLE_TAG).drop(COUNT_ONE)

        Timber.w("testing loadElements parsedDocument $parsedDocument parsedTable ${parsedTable.count()}")

        return parsedTable
    }

    fun Element.parseElementDataAsChannel(): Triple<String, String, List<String>> {
        val selected = this.select(TABLE_ROW_TAG)
        val id = selected[2].text()
        val logo = selected[0].select(IMAGE_QUERY_TAG).attr(IMAGE_SRC_TAG)
        val names = selected[1].textNodes().map { it.text() }

        return Triple(id, logo, names)
    }

    fun Element.parseElementDataAsProgram(): Triple<String, String, String> {
        val selected = this.select(TABLE_ROW_TAG)
        val date = "${selected[0].text()} ${selected[1].text()}"
        val title = selected[2].text()
        val description = selected[3].text()

        return Triple(date, title, description)
    }
}
