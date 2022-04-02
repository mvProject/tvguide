package com.mvproject.tvprogramguide.domain.repository

import com.mvproject.tvprogramguide.domain.database.dao.CustomListDao
import com.mvproject.tvprogramguide.data.entity.CustomListEntity
import com.mvproject.tvprogramguide.data.model.CustomList
import com.mvproject.tvprogramguide.domain.utils.Mappers.asCustomLists
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class CustomListRepository @Inject constructor(
    private val customListDao: CustomListDao
) {

    fun loadChannelsLists() = customListDao.getAllCustomLists().map {
        it.asCustomLists()
    }

    suspend fun loadChannelsList(id: Int) = customListDao.getSingleCustomList(id)

    suspend fun addCustomList(name: String) {
        if (name.isNotEmpty()) {
            val listItem = CustomListEntity(
                Random.nextInt(),
                name
            )
            customListDao.addCustomList(listItem)
        }
    }

    suspend fun deleteList(item: CustomList) {
        customListDao.deleteSingleCustomList(item.id)
    }
}