package com.mvproject.tvprogramguide.customlists

import androidx.lifecycle.ViewModel
import com.mvproject.tvprogramguide.StoreManager
import com.mvproject.tvprogramguide.utils.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomListViewModel @Inject constructor(
    private val storeManager: StoreManager
) : ViewModel() {

    fun setSelectedList(name: String) {
        storeManager.setCurrentChannelList(name)
    }

    fun clearSelectedList() {
        storeManager.setCurrentChannelList(NO_VALUE_STRING)
    }
}
