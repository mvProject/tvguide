package com.mvproject.tvprogramguide.ui.components.radio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RadioGroupContent(
    radioOptions: ImmutableList<String> = persistentListOf(),
    defaultSelection: Int = COUNT_ZERO,
    onItemClick: (String) -> Unit = {},
) {
    val (selectedOption, onOptionSelected) =
        remember(defaultSelection) {
            mutableStateOf(radioOptions[defaultSelection])
        }

    Column {
        radioOptions.forEach { item ->
            ListItem(
                modifier = Modifier.clickable {
                    onOptionSelected(item)
                    onItemClick(item)
                },
                leadingContent = {
                    RadioButton(
                        selected = (item == selectedOption),
                        colors =
                        RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.onSurface,
                            unselectedColor = MaterialTheme.colorScheme.outline,
                        ),
                        onClick = {
                            onOptionSelected(item)
                            onItemClick(item)
                        },
                    )
                }, headlineContent = {
                    Text(
                        text = item,
                        style =
                        if (item == selectedOption) {
                            MaterialTheme.typography.titleMedium
                        } else {
                            MaterialTheme.typography.bodyMedium
                        },
                        color = if (item == selectedOption) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                    )
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
fun RadioGroupContentView() {
    TvGuideTheme {
        RadioGroupContent(
            radioOptions = persistentListOf("Option1", "Option2", "Option3"),
        )
    }
}
