package com.mvproject.tvprogramguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mvproject.tvprogramguide.ui.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }

    //todo refactor all screens theme

    // todo restore navigation

    // override fun onResume() {
    //     super.onResume()
    //     mainViewModel.checkAvailableChannelsUpdate()
    //     mainViewModel.checkFullProgramsUpdate()
    // }

    // override fun attachBaseContext(base: Context) {
    //     super.attachBaseContext(LocaleHelper.wrapContext(base))
    // }

    //   override fun onConfigurationChanged(newConfig: Configuration) {
    //       super.onConfigurationChanged(newConfig)
    //       LocaleHelper.overrideLocale(this)
    //   }
}