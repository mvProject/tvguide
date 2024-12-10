package com.mvproject.tvprogramguide.ui.components.toolbars

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarWithBack(
    title: String,
    onBackClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(MaterialTheme.dimens.size8),
                onClick = onBackClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                    contentDescription = Icons.AutoMirrored.Filled.NavigateBefore.name
                )
            }
        },
        colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        ),
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
