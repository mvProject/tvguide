package com.mvproject.tvprogramguide.ui.selectedchannels

import com.mvproject.tvprogramguide.data.model.domain.SelectedChannelWithPrograms
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import javax.annotation.concurrent.Immutable

@Immutable
data class ChannelsViewState(
    val listName: String = NO_VALUE_STRING,
    val programs: List<SelectedChannelWithPrograms> = listOf()
)
