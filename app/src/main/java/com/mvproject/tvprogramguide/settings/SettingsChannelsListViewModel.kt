package com.mvproject.tvprogramguide.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tvprogramguide.database.entity.CustomListEntity
import com.mvproject.tvprogramguide.repository.CustomListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsChannelsListViewModel @Inject constructor(
    private val customListRepository: CustomListRepository
) : ViewModel() {

    private var _customs = MutableStateFlow<List<CustomListEntity>>(emptyList())
    val customs = _customs.asStateFlow()

    init {
        viewModelScope.launch {
            customListRepository.loadChannelsLists().collect {
                _customs.emit(it)
            }
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
