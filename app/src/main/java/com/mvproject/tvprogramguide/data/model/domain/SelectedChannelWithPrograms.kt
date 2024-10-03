package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable

@Immutable
data class SelectedChannelWithPrograms(
    val selectedChannel: SelectionChannel,
    val programs: List<Program>,
)
