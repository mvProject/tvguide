package com.mvproject.tvprogramguide.data.model.response

import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING

data class ProgramResponse(
    val start: String = NO_VALUE_STRING,
    val title: String = NO_VALUE_STRING,
    val description: String = NO_VALUE_STRING,
    val category: String = NO_VALUE_STRING
)
