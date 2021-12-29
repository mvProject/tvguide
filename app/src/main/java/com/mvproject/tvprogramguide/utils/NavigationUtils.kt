package com.mvproject.tvprogramguide.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

/**
 * Extension to simplify navigation to destination via Id
 */
fun Fragment.routeTo(@IdRes destination: Int, @Nullable params: Bundle? = null) {
    findNavController().navigate(
        destination,
        params
    )
}

/**
 * Extension to simplify navigation to destination via NavDirection Action
 */
fun Fragment.routeTo(@NonNull destination: NavDirections) {
    findNavController().navigate(
        destination
    )
}

/**
 * Extension to simplify navigation to previous destination
 */
fun Fragment.routeToBack() {
    findNavController().popBackStack()
}

fun Fragment.routeToBack(@IdRes destination: Int) {
    findNavController().popBackStack(destination, false)
}


