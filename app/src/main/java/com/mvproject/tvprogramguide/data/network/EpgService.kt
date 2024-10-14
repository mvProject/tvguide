package com.mvproject.tvprogramguide.data.network

import com.mvproject.tvprogramguide.data.model.response.AllAvailableChannelsResponse
import com.mvproject.tvprogramguide.data.model.response.ProgramListResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

//const val BASE_URL = "https://epg.iptvx.one/api/"
const val BASE_URL = "http://epg.one/"
interface EpgService {
    @GET("channels.json")
    suspend fun getChannels(): AllAvailableChannelsResponse

    @GET
    @Streaming
    suspend fun downloadFile(@Url fileUrl: String): ResponseBody

    @GET("id/{channel}.json")
    suspend fun getChannelProgram(@Path("channel") channelId: String): ProgramListResponse
}
