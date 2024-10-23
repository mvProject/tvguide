package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.empty

@Composable
fun NoItemsScreen(
    title: String = stringResource(id = R.string.msg_no_items_found),
    navigateTitle: String = String.empty,
    onNavigateClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size16)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size96),
                    imageVector = Icons.Filled.Info,
                    contentDescription = title
                )

                Text(
                    modifier = Modifier
                        .padding(
                            top = MaterialTheme.dimens.size16,
                            bottom = MaterialTheme.dimens.size24
                        ),
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                if (navigateTitle.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(
                                top = MaterialTheme.dimens.size16,
                                bottom = MaterialTheme.dimens.size24
                            )
                            .clickable { onNavigateClick() },
                        text = navigateTitle,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoItemsScreen() {
    TvGuideTheme() {
        NoItemsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkNoItemsScreen() {
    TvGuideTheme(darkTheme = true) {
        NoItemsScreen()
    }
}
