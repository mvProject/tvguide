package com.mvproject.tvprogramguide.ui.components.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ONE
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

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf(NO_VALUE_STRING)
    }

    Box(modifier = modifier) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = MaterialTheme.dimens.size4,
                    shape = CircleShape
                )
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = CircleShape
                ),
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = COUNT_ONE,
            textStyle = TextStyle(
                color = MaterialTheme.colors.onPrimary,
                fontSize = MaterialTheme.dimens.font18,
                fontFamily = fonts,
                fontWeight = FontWeight.SemiBold
            ),
            cursorBrush = SolidColor(MaterialTheme.colors.onPrimary),
            decorationBox = { textView ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size4)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.size16)
                            .size(MaterialTheme.dimens.size24),
                        tint = MaterialTheme.appColors.tintPrimary

                    )

                    Box(
                        modifier = Modifier.weight(MaterialTheme.dimens.weight1),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        textView()
                    }

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
                }
            }
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

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.size16)
            ) {
                Timber.d("testing search 1 val $it")
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

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.size16)
            ) {
                Timber.d("testing search 1 val $it")
            }
        }
    }
}
