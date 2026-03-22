package com.mvproject.tvprogramguide.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

val NavController.canNavigate: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

fun NavController.navigateToBack() {
    if (canNavigate) {
        navigateUp()
    }
}
