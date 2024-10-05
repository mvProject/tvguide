package com.mvproject.tvprogramguide.data.repository

import com.mvproject.tvprogramguide.data.database.dao.UserChannelsListDao
import com.mvproject.tvprogramguide.data.database.entity.UserChannelsListEntity
import com.mvproject.tvprogramguide.data.mappers.Mappers.asUserChannelsLists
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class CustomListRepository
    @Inject
    constructor(
        private val userChannelsListDao: UserChannelsListDao,
    ) {
        fun loadChannelsLists() =
            userChannelsListDao
                .getAllUserChannelsLists()
                .map { entities ->
                    entities.asUserChannelsLists()
                }

        suspend fun addCustomList(name: String) {
            if (name.isNotEmpty()) {
                val listItem =
                    UserChannelsListEntity(
                        Random.nextInt(),
                        name,
                    )
                userChannelsListDao.addUserChannelsList(channelList = listItem)
            }
        }

        suspend fun deleteList(item: UserChannelsList) {
            userChannelsListDao.deleteSingleUserChannelsList(channelId = item.id)
        }
    }
