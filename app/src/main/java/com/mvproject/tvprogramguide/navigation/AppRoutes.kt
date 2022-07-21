package com.mvproject.tvprogramguide.navigation

import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_ID
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_CHANNEL_NAME
import com.mvproject.tvprogramguide.navigation.NavConstants.ARGUMENT_USER_LIST_NAME
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_APP_SETTINGS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_CHANNELS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_CHANNEL_SETTINGS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_OPTION_SETTINGS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_SELECTED_CHANNELS
import com.mvproject.tvprogramguide.navigation.NavConstants.ROUTE_USER_CUSTOM_LIST

sealed class AppRoutes(val route: String) {
    object Channels : AppRoutes(ROUTE_CHANNELS)
    object SelectedChannel :
        AppRoutes("$ROUTE_SELECTED_CHANNELS/{$ARGUMENT_CHANNEL_ID}/{$ARGUMENT_CHANNEL_NAME}") {
        fun applyArgs(channelId: String, channelName: String) =
            "$ROUTE_SELECTED_CHANNELS/$channelId/$channelName"
    }

    object OptionSettings : AppRoutes(ROUTE_OPTION_SETTINGS)
    object ChannelSettings : AppRoutes("$ROUTE_CHANNEL_SETTINGS/{$ARGUMENT_USER_LIST_NAME}") {
        fun applyArgs(userListName: String) =
            "$ROUTE_CHANNEL_SETTINGS/$userListName"
    }

    object AppSettings : AppRoutes(ROUTE_APP_SETTINGS)
    object UserCustomList : AppRoutes(ROUTE_USER_CUSTOM_LIST)
}
