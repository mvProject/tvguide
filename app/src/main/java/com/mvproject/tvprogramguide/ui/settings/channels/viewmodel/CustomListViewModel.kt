package com.mvproject.tvprogramguide.ui.settings.channels.viewmodel

import androidx.lifecycle.ViewModel
import com.mvproject.tvprogramguide.domain.helpers.StoreHelper
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomListViewModel @Inject constructor(
    private val storeHelper: StoreHelper
) : ViewModel() {

    fun setSelectedList(name: String) {
        storeHelper.setCurrentChannelList(name = name)
    }

    override fun onCleared() {
        super.onCleared()
        storeHelper.setCurrentChannelList(name = NO_VALUE_STRING)
    }
}
