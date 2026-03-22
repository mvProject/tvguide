package com.mvproject.tvprogramguide.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SelectedChannelWithIconEntity(
    @Embedded val channel: SelectedChannelEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val allChannel: AvailableChannelEntity?,
)
