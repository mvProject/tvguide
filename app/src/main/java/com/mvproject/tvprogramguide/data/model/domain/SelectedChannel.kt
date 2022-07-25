package com.mvproject.tvprogramguide.data.model.domain

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class SelectedChannel(
    val channelId: String,
    val channelName: String,
    val channelIcon: String,
    val order: Int = 0,
    val parentList: String = ""
) : Parcelable