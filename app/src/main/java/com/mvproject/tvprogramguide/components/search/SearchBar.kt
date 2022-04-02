package com.mvproject.tvprogramguide.components.search

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appColors
import com.mvproject.tvprogramguide.theme.dimens
import timber.log.Timber

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.primary,
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
                .background(MaterialTheme.colors.primary, CircleShape),
            textStyle = TextStyle(
                color = MaterialTheme.colors.onPrimary,
                fontSize = MaterialTheme.dimens.font18
            ),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.size16)
                        .size(MaterialTheme.dimens.size24)
                )
            },
            trailingIcon = {
                if (text != "") {
                    IconButton(
                        onClick = {
                            text = ""
                            onSearch(text)
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(MaterialTheme.dimens.size16)
                                .size(MaterialTheme.dimens.size24)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RectangleShape,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.appColors.textPrimary,
                cursorColor = MaterialTheme.appColors.textPrimary,
                leadingIconColor = MaterialTheme.appColors.tintPrimary,
                trailingIconColor = Color.Red,
                backgroundColor = MaterialTheme.appColors.backgroundPrimary,
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
        mutableStateOf("")
    }

    Box(modifier = modifier) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, CircleShape)
                .background(MaterialTheme.appColors.backgroundPrimary, CircleShape),
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            textStyle = TextStyle(
                color = MaterialTheme.appColors.textPrimary,
                fontSize = 18.sp
            ),
            cursorBrush = SolidColor(MaterialTheme.appColors.textPrimary),
            decorationBox = { textView ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp),
                        tint = MaterialTheme.appColors.tintPrimary

                    )

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        textView()
                    }

                    if (text != "") {
                        IconButton(
                            onClick = {
                                text = ""
                                onSearch(text)// Remove text from TextField when you press the 'X' icon
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(15.dp)
                                    .size(24.dp),
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