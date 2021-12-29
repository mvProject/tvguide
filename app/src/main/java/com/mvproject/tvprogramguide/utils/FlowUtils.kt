package com.mvproject.tvprogramguide.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Collect items from the specified [Flow] only when fragment is at least in STARTED state.
 */
fun <T> Fragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        // this coroutine is launched every time when onStart is called;
        // collecting is cancelled in onStop
        flow.collect {
            onCollect(it)
        }
    }
}
