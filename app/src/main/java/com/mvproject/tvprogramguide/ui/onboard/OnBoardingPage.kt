package com.mvproject.tvprogramguide.ui.onboard

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
    object First : OnBoardingPage(
        image = R.drawable.onboard_user_list_create,
        title = R.string.onboard_title_user_list_create,
        description = R.string.onboard_description_user_list_create
    )

    object Second : OnBoardingPage(
        image = R.drawable.onboard_user_list_switch,
        title = R.string.onboard_title_user_list_switch,
        description = R.string.onboard_description_user_list_switch
    )

    object Third : OnBoardingPage(
        image = R.drawable.onboard_notification,
        title = R.string.onboard_title_notification,
        description = R.string.onboard_description_notification
    )
}