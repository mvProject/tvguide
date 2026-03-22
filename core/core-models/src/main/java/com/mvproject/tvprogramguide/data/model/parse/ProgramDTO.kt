package com.mvproject.tvprogramguide.data.model.parse

import com.mvproject.tvprogramguide.utils.AppConstants.empty

data class ProgramDTO(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = String.empty,
    val description: String = String.empty,
)
