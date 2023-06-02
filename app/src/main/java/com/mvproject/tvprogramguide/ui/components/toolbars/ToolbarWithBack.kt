package com.mvproject.tvprogramguide.ui.components.toolbars

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarWithBack(
    title: String,
    onBackClick: () -> Unit = {}
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
            FilledIconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(MaterialTheme.dimens.size8),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.Default.NavigateBefore,
                    contentDescription = "Back",
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    )
}

@Preview
@Composable
fun ToolbarWithBackView() {
    TvGuideTheme {
        ToolbarWithBack(title = "TetstTitle")
    }
}

@Preview
@Composable
fun ToolbarWithBackDarkView() {
    TvGuideTheme(true) {
        ToolbarWithBack(title = "TetstTitle")
    }
}
