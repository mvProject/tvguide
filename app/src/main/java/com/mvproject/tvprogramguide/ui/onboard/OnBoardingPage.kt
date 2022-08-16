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
    // todo set proper graphic resources
    object First : OnBoardingPage(
        image = R.drawable.ic_tvguide_logo,
        title = R.string.onboard_title_user_list_create,
        description = R.string.onboard_description_user_list_create
    )

    object Second : OnBoardingPage(
        image = R.drawable.ic_tvguide_logo,
        title = R.string.onboard_title_user_list_switch,
        description = R.string.onboard_description_user_list_switch
    )

    object Third : OnBoardingPage(
        image = R.drawable.ic_tvguide_logo,
        title = R.string.onboard_title_notification,
        description = R.string.onboard_description_notification
    )
}