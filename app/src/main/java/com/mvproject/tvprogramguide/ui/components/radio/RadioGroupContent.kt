package com.mvproject.tvprogramguide.ui.components.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography

@Composable
fun RadioGroupContent(
    radioOptions: List<String> = listOf(),
    defaultSelection: Int = COUNT_ZERO,
    onItemClick: (String) -> Unit
) {

    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptions[defaultSelection])
    }

    Column {
        radioOptions.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (item == selectedOption),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.onSecondary,
                        unselectedColor = MaterialTheme.colors.onPrimary
                    ),
                    onClick = {
                        onOptionSelected(item)
                        onItemClick(item)
                    }
                )

                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (item == selectedOption) MaterialTheme.colors.onSecondary
                            else MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    ) { append("  $item  ") }
                }

                ClickableText(
                    text = annotatedString,
                    style = MaterialTheme.appTypography.textSemiBold,
                    onClick = {
                        onOptionSelected(item)
                        onItemClick(item)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RadioGroupContentView() {
    TvGuideTheme() {
        RadioGroupContent(
            radioOptions = listOf("Option1", "Option2", "Option3"),
            onItemClick = { String }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RadioGroupContentDarkView() {
    TvGuideTheme(true) {
        RadioGroupContent(
            radioOptions = listOf("Option1", "Option2", "Option3"),
            onItemClick = { String }
        )
    }
}
