package com.mvproject.tvprogramguide.data.model.domain

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.empty
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class SelectedChannel(
    val channelId: String,
    val channelName: String = String.empty,
    val channelIcon: String = String.empty,
    val order: Int = COUNT_ZERO,
    val parentList: String = String.empty
) : Parcelable
