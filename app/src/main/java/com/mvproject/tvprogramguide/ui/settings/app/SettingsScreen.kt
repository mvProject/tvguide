package com.mvproject.tvprogramguide.ui.settings.app

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.toolbars.ToolbarWithBack
import timber.log.Timber

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    SideEffect {
        Timber.i("testing SettingsScreen SideEffect")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ToolbarWithBack(title = stringResource(id = R.string.settings_title)) {
            onBackClick()
        }

        SettingsContent()
    }
}