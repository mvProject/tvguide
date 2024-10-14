package com.mvproject.tvprogramguide.data.model.response

import com.mvproject.tvprogramguide.utils.AppConstants.empty

data class ProgramResponse(
    val start: String = String.empty,
    val title: String = String.empty,
    val description: String = String.empty,
    val category: String = String.empty,
)

data class ProgramDTO(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = String.empty,
    val description: String = String.empty,
)
