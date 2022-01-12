package com.mvproject.tvprogramguide.customlists

import androidx.lifecycle.ViewModel
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.utils.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
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
}
