package com.mvproject.tvprogramguide.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tvprogramguide.data.model.CustomList
import com.mvproject.tvprogramguide.utils.DbConstants.TABLE_USER_CUSTOM_LIST

@Entity(tableName = TABLE_USER_CUSTOM_LIST)
class CustomListEntity(
    @PrimaryKey
    val id: Int,
    val name: String
) {
    fun toCustomList() = CustomList(
        id = id,
        listName = name
    )
}
