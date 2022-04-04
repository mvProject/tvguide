package com.mvproject.tvprogramguide.ui.settings.channels.viewmodel

import androidx.lifecycle.ViewModel
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomListViewModel @Inject constructor(
    private val storeHelper: StoreHelper
) : ViewModel() {

    fun setSelectedList(name: String) {
        storeHelper.setCurrentChannelList(name)
    }

    fun clearSelectedList() {
        storeHelper.setCurrentChannelList(NO_VALUE_STRING)
    }

    init {
        Timber.i("testing CustomListViewModel init")
    }

    override fun onCleared() {
        super.onCleared()
        storeHelper.setCurrentChannelList(NO_VALUE_STRING)
        Timber.i("testing CustomListViewModel onCleared")
    }
}
