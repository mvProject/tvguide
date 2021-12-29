package com.mvproject.tvprogramguide.repository

import com.mvproject.tvprogramguide.database.CustomListDao
import com.mvproject.tvprogramguide.model.entity.CustomListEntity
import javax.inject.Inject
import kotlin.random.Random

class CustomListRepository @Inject constructor(
    private val customListDao: CustomListDao
) {

    fun loadChannelsLists() = customListDao.getAllCustomLists()

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

    suspend fun deleteList(item: CustomListEntity) {
        customListDao.deleteSingleCustomList(item.id)
    }
}