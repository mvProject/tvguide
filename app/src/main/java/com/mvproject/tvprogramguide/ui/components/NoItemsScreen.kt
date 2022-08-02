package com.mvproject.tvprogramguide.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun NoItemsScreen(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.no_items_found),
    backgroundColor: Color = MaterialTheme.colors.primary,
    titleColor: Color = MaterialTheme.colors.onPrimary,
    tintColor: Color = MaterialTheme.colors.onPrimary,
    navigateTitle: String = NO_VALUE_STRING,
    onNavigateClick: () -> Unit = {}

) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size16)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(MaterialTheme.dimens.size96),
                    imageVector = Icons.Filled.Info,
                    tint = tintColor,
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
                    color = titleColor,
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
                        color = titleColor,
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
