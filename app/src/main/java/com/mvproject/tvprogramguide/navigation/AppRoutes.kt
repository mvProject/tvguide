package com.mvproject.tvprogramguide.navigation

import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_APP_SETTINGS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_CHANNELS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_CHANNEL_SETTINGS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_ONBOARD
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_SETTINGS_GENERAL
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_SINGLE_CHANNEL
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_USER_CUSTOM_LIST

sealed class AppRoutes(val route: String) {
    data object OnBoard : AppRoutes(ROUTE_ONBOARD)
    data object Channels : AppRoutes(ROUTE_CHANNELS)
    data object SingleChannel : AppRoutes(ROUTE_SINGLE_CHANNEL)
    data object SettingsGeneral : AppRoutes(ROUTE_SETTINGS_GENERAL)
    data object ChannelSettings : AppRoutes(ROUTE_CHANNEL_SETTINGS)
    data object AppSettings : AppRoutes(ROUTE_APP_SETTINGS)
    data object UserCustomList : AppRoutes(ROUTE_USER_CUSTOM_LIST)
}
