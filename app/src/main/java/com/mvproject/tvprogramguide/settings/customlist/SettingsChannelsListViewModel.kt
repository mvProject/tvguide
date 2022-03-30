package com.mvproject.tvprogramguide.settings.customlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.helpers.StoreHelper
import com.mvproject.tvprogramguide.database.entity.CustomListEntity
import com.mvproject.tvprogramguide.repository.CustomListRepository
import com.mvproject.tvprogramguide.utils.COUNT_ONE
import com.mvproject.tvprogramguide.utils.NO_VALUE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsChannelsListViewModel @Inject constructor(
    private val customListRepository: CustomListRepository,
    private val storeHelper: StoreHelper
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
        val current = storeHelper.defaultChannelList
        val listCount = lists.count()
        if (current == NO_VALUE_STRING && listCount == COUNT_ONE) {
            val listName = lists.first().name
            storeHelper.setDefaultChannelList(listName)
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
