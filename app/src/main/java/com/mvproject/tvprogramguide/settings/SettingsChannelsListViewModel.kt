package com.mvproject.tvprogramguide.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.StoreManager
import com.mvproject.tvprogramguide.database.entity.CustomListEntity
import com.mvproject.tvprogramguide.repository.CustomListRepository
import com.mvproject.tvprogramguide.utils.COUNT_ONE
import com.mvproject.tvprogramguide.utils.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsChannelsListViewModel @Inject constructor(
    private val customListRepository: CustomListRepository,
    private val storeManager: StoreManager
) : ViewModel() {

    private var _customs = MutableStateFlow<List<CustomListEntity>>(emptyList())
    val customs = _customs.asStateFlow()

    init {
        viewModelScope.launch {
            customListRepository.loadChannelsLists().collect { lists ->
                initDefaultList(lists)
                _customs.emit(lists)
            }
        }
    }

    private fun initDefaultList(lists: List<CustomListEntity>) {
        val current = storeManager.defaultChannelList
        val listCount = lists.count()
        if (current == NO_VALUE_STRING && listCount == COUNT_ONE) {
            val listName = lists.first().name
            storeManager.setDefaultChannelList(listName)
        }
    }

    fun addCustomList(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            customListRepository.addCustomList(name)
        }

    }

    fun deleteList(item: CustomListEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            customListRepository.deleteList(item)
        }
    }
}
