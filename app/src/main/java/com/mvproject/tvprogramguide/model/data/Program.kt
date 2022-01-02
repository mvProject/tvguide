package com.mvproject.tvprogramguide.model.data

data class Program(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val channel: String = ""
): IChannel()