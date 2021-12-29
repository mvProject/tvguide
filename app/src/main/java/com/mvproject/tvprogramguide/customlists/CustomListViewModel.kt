package com.mvproject.tvprogramguide.customlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.StoreManager
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.model.entity.ChannelEntity
import com.mvproject.tvprogramguide.model.entity.CustomListEntity
import com.mvproject.tvprogramguide.model.entity.SelectedChannelEntity
import com.mvproject.tvprogramguide.repository.AllChannelRepository
import com.mvproject.tvprogramguide.repository.CustomListRepository
import com.mvproject.tvprogramguide.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.Mappers.asAlreadySelected
import com.mvproject.tvprogramguide.utils.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

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
