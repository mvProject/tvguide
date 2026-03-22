package com.mvproject.tvprogramguide.data.model.domain

import androidx.compose.runtime.Immutable

@Immutable
data class SingleChannelWithPrograms(
    val date: String,
    val programs: List<Program>
)
