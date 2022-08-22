package com.mvproject.tvprogramguide.ui.components.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appColors
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.theme.fonts
import timber.log.Timber

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf(NO_VALUE_STRING)
    }
    Surface(
        modifier = modifier,
        shape = CircleShape,
        elevation = MaterialTheme.dimens.size12
    ) {
        TextField(
            value = text,
            onValueChange = { value ->
                text = value
                onSearch(value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface, CircleShape),

            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
                fontSize = MaterialTheme.dimens.font18,
                fontFamily = fonts,
                fontWeight = FontWeight.SemiBold
            ),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.size16)
                        .size(MaterialTheme.dimens.size24),
                    tint = MaterialTheme.colors.onSurface
                )
            },
            trailingIcon = {
                if (text != NO_VALUE_STRING) {
                    IconButton(
                        onClick = {
                            text = NO_VALUE_STRING
                            onSearch(text)
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(MaterialTheme.dimens.size16)
                                .size(MaterialTheme.dimens.size24),
                            tint = MaterialTheme.appColors.tintSecondary
                        )
                    }
                }
            },
            singleLine = true,
            shape = RectangleShape,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.onSurface,
                leadingIconColor = MaterialTheme.appColors.tintPrimary,
                trailingIconColor = Color.Red,
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
fun Preview() {
    TvGuideTheme {
        Column {
            SearchView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.size16)
            ) {
                Timber.d("testing search 2 val $it")
            }
        }
    }
}

@Preview
@Composable
fun PreviewDark() {
    TvGuideTheme(true) {
        Column {
            SearchView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.size16)
            ) {
                Timber.d("testing search 2 val $it")
            }
        }
    }
}
