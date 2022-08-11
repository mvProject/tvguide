package com.mvproject.tvprogramguide.data.model.domain

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class SelectedChannel(
    val channelId: String,
    val channelName: String,
    val channelIcon: String,
    val order: Int = COUNT_ZERO,
    val parentList: String = NO_VALUE_STRING
) : Parcelable