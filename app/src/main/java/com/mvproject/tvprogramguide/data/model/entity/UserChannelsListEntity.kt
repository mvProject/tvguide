package com.mvproject.tvprogramguide.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tvprogramguide.data.database.DbConstants.TABLE_USER_CUSTOM_LIST
import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList

@Entity(tableName = TABLE_USER_CUSTOM_LIST)
class UserChannelsListEntity(
    @PrimaryKey
    val id: Int,
    val name: String
) {
    fun toUserChannelsList() = UserChannelsList(
        id = id,
        listName = name
    )
}
