package com.mvproject.tvprogramguide.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class SelectedChannelModel(
    val channel: Channel,
    val programs: List<Program>
)
