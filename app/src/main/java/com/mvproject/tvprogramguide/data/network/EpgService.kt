package com.mvproject.tvprogramguide.data.network

import com.mvproject.tvprogramguide.data.model.response.AllAvailableChannelsResponse
import com.mvproject.tvprogramguide.data.model.response.ProgramListResponse
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://epg.iptvx.one/api/"
interface EpgService {
    @GET("channels.json")
    suspend fun getChannels(): AllAvailableChannelsResponse

    @GET("id/{channel}.json")
    suspend fun getChannelProgram(@Path("channel") channelId: String): ProgramListResponse
}
