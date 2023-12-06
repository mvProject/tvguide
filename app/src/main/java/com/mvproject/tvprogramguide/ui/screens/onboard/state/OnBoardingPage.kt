package com.mvproject.tvprogramguide.ui.screens.onboard.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mvproject.tvprogramguide.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int
) {
    data object First : OnBoardingPage(
        image = R.drawable.onboard_user_list_create,
        title = R.string.onboard_title_user_list_create,
        description = R.string.onboard_description_user_list_create
    )

    data object Second : OnBoardingPage(
        image = R.drawable.onboard_user_list_switch,
        title = R.string.onboard_title_user_list_switch,
        description = R.string.onboard_description_user_list_switch
    )

    data object Third : OnBoardingPage(
        image = R.drawable.onboard_notification,
        title = R.string.onboard_title_notification,
        description = R.string.onboard_description_notification
    )
}
