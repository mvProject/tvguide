package com.mvproject.tvprogramguide.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class SingleChannelModel(
    val date: String,
    val programs: List<Program>
)
