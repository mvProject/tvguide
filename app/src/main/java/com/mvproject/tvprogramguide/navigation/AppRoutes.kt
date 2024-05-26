package com.mvproject.tvprogramguide.navigation

import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_APP_SETTINGS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_CHANNELS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_CHANNEL_SETTINGS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_ONBOARD
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_SETTINGS_GENERAL
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_SINGLE_CHANNEL
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_USER_CUSTOM_LIST
import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes(val route: String) {
    @Serializable
    data object OnBoard : AppRoutes(ROUTE_ONBOARD)

    @Serializable
    data object Channels : AppRoutes(ROUTE_CHANNELS)

    data object SingleChannel : AppRoutes(ROUTE_SINGLE_CHANNEL)

    @Serializable
    data object SettingsGeneral : AppRoutes(ROUTE_SETTINGS_GENERAL)

    data object ChannelSettings : AppRoutes(ROUTE_CHANNEL_SETTINGS)

    @Serializable
    data object AppSettings : AppRoutes(ROUTE_APP_SETTINGS)

    data object UserCustomList : AppRoutes(ROUTE_USER_CUSTOM_LIST)
}
