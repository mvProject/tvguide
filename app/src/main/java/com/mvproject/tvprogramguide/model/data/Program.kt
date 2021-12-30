package com.mvproject.tvprogramguide.model.data

data class Program(
    val dateTime: Long,
    val duration: Long,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val channel: String = ""
): IChannel()