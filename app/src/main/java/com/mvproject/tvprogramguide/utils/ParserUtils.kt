package com.mvproject.tvprogramguide.utils

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ONE
import timber.log.Timber

/**
 * Utility object providing parsing functions for extracting channel and program information from HTML elements.
 */
object ParserUtils {
    private const val TABLE_TAG = "tr"
    private const val TABLE_ROW_TAG = "td"
    private const val IMAGE_QUERY_TAG = "img"
    private const val IMAGE_SRC_TAG = "src"

    /**
     * Loads and parses HTML elements from a given URL.
     *
     * @param sourceUrl The URL to fetch and parse HTML from.
     * @return A list of parsed HTML elements, typically representing rows in a table.
     */
    suspend fun loadElements(sourceUrl: String): List<Element> {
        Timber.w("testing loadElements from $sourceUrl")
        val parsedDocument: Document = Ksoup.parseGetRequest(url = sourceUrl)
        val parsedTable = parsedDocument.select(TABLE_TAG).drop(COUNT_ONE)

        Timber.w("testing loadElements parsedDocument $parsedDocument parsedTable ${parsedTable.count()}")

        return parsedTable
    }

    /**
     * Extension function to parse an HTML element as a channel.
     *
     * @receiver Element The HTML element to parse.
     * @return Triple containing the channel ID, logo URL, and a list of channel names.
     */
    fun Element.parseElementDataAsChannel(): Triple<String, String, List<String>> {
        val selected = this.select(TABLE_ROW_TAG)
        val id = selected[2].text()
        val logo = selected[0].select(IMAGE_QUERY_TAG).attr(IMAGE_SRC_TAG)
        val names = selected[1].textNodes().map { it.text() }

        return Triple(id, logo, names)
    }

    /**
     * Extension function to parse an HTML element as a program.
     *
     * @receiver Element The HTML element to parse.
     * @return Triple containing the program date and time, title, and description.
     */
    fun Element.parseElementDataAsProgram(): Triple<String, String, String> {
        val selected = this.select(TABLE_ROW_TAG)
        val date = "${selected[0].text()} ${selected[1].text()}"
        val title = selected[2].text()
        val description = selected[3].text()

        return Triple(date, title, description)
    }
}
