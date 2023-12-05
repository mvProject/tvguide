package com.mvproject.tvprogramguide.ui.components.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO

@Composable
fun RadioGroupContent(
    radioOptions: List<String> = listOf(),
    defaultSelection: Int = COUNT_ZERO,
    onItemClick: (String) -> Unit = {}
) {

    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptions[defaultSelection])
    }

    Column {
        radioOptions.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (item == selectedOption),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.tertiary,
                        unselectedColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    onClick = {
                        onOptionSelected(item)
                        onItemClick(item)
                    }
                )

                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (item == selectedOption)
                                MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.onSurface,
                        )
                    ) { append("  $item  ") }
                }

                ClickableText(
                    text = annotatedString,
                    style = if (item == selectedOption)
                        MaterialTheme.typography.titleMedium
                    else MaterialTheme.typography.bodyMedium,
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
            radioOptions = listOf("Option1", "Option2", "Option3")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RadioGroupContentDarkView() {
    TvGuideTheme(darkTheme = true) {
        RadioGroupContent(
            radioOptions = listOf("Option1", "Option2", "Option3")
        )
    }
}
