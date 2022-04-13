package com.mvproject.tvprogramguide.ui.selectedchannels

import com.mvproject.tvprogramguide.data.model.SelectedChannelModel
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import javax.annotation.concurrent.Immutable

@Immutable
data class ChannelsViewState(
    val listName: String = NO_VALUE_STRING,
    val programs: List<SelectedChannelModel> = listOf()
)
