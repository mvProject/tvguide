package com.mvproject.tvprogramguide.ui.components.toolbars

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarWithOptions(
    title: String,
    isSelectEnabled: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onSelectClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        navigationIcon = {
            if (isSelectEnabled) {
                FilledIconButton(
                    onClick = onSelectClick,
                    modifier = Modifier.padding(MaterialTheme.dimens.size8),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Back",
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        actions = {
            FilledIconButton(
                onClick = onSettingsClick,
                modifier = Modifier.padding(MaterialTheme.dimens.size8),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Back",
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ToolbarWithOptionsView() {
    TvGuideTheme() {
        ToolbarWithOptions(title = "TetstTitle")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ToolbarWithOptionsDarkView() {
    TvGuideTheme(true) {
        ToolbarWithOptions(title = "TetstTitle")
    }
}
