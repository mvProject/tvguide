package com.mvproject.tvprogramguide.domain.contract

import com.mvproject.tvprogramguide.data.model.parse.ChannelParseModel
import com.mvproject.tvprogramguide.data.model.parse.ProgramParseModel

interface IProgramDataSource {
    suspend fun downloadAndParseXml(
        url: String,
        onProgrammeParsed: suspend (ProgramParseModel) -> Unit
    )

    suspend fun downloadAndParseChannels(
        url: String,
        onChannelParsed: suspend (ChannelParseModel) -> Unit
    )
}
