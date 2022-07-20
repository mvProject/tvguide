package com.mvproject.tvprogramguide.data.model.response

import com.google.gson.annotations.SerializedName

data class ProgramListResponse(
    @SerializedName("ch_programme")
    val chPrograms: List<ProgramResponse>
)
