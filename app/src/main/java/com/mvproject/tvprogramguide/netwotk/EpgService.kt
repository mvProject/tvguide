package com.mvproject.tvprogramguide.netwotk

import com.mvproject.tvprogramguide.netwotk.json.JsonChannelList
import com.mvproject.tvprogramguide.netwotk.json.JsonProgramList
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://epg.iptvx.one/api/"
interface EpgService {
    @GET("channels.json")
    suspend fun getChannels(): JsonChannelList

    @GET("id/{channel}.json")
    suspend fun getChannelProgram(@Path("channel") channelId: String): JsonProgramList
}
