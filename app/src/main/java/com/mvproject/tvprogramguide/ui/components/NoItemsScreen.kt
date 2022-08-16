package com.mvproject.tvprogramguide.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun NoItemsScreen(
    title: String = stringResource(id = R.string.msg_no_items_found),
    navigateTitle: String = NO_VALUE_STRING,
    onNavigateClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.primary
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
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = title
                )

                Text(
                    modifier = Modifier
                        .padding(
                            top = MaterialTheme.dimens.size16,
                            bottom = MaterialTheme.dimens.size24
                        ),
                    text = title,
                    style = MaterialTheme.appTypography.textSemiBold,
                    color = MaterialTheme.colors.onPrimary,
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
                        style = MaterialTheme.appTypography.textExtraBold,
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
    }
}

@Composable
fun ErrorScreen(error: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(text = "Oops, $error!")
    }
}
