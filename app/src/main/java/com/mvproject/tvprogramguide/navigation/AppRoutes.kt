package com.mvproject.tvprogramguide.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes {
    @Serializable
    data object Channels : AppRoutes()

    @Serializable
    data class SingleChannel(
        val channelId: String,
        val channelName: String,
    ) : AppRoutes()

    @Serializable
    data object SettingsGeneral : AppRoutes()

    @Serializable
    data class ChannelSettings(
        val userListName: String,
    ) : AppRoutes()

    @Serializable
    data object AppSettings : AppRoutes()

    @Serializable
    data object UserCustomList : AppRoutes()
}
