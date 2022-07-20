package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable

@Immutable
data class SelectedChannelWithPrograms(
    val selectedChannel: SelectedChannel,
    val programs: List<Program>
)
