package com.mvproject.tvprogramguide.data.json

import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING

data class JsonProgram(
    val start: String = NO_VALUE_STRING,
    val title: String = NO_VALUE_STRING,
    val description: String = NO_VALUE_STRING,
    val category: String = NO_VALUE_STRING
)