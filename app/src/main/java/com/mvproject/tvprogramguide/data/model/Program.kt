package com.mvproject.tvprogramguide.data.model

import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING

data class Program(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = NO_VALUE_STRING,
    val description: String = NO_VALUE_STRING,
    val category: String = NO_VALUE_STRING,
    val channel: String = NO_VALUE_STRING
)
