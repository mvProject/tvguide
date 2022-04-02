package com.mvproject.tvprogramguide.components.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RadioGroupScrollable(
    radioOptions: List<String> = listOf(),
    defaultSelection: Int = 0,
    cardBackgroundColor: Color = MaterialTheme.colors.primary,
    onItemClick: (String) -> Unit
) {
    if (radioOptions.isNotEmpty()) {
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .background(cardBackgroundColor)
                .fillMaxWidth()
                .height(180.dp)
                .verticalScroll(
                    state = scrollState,
                    enabled = true,
                )
        ) {
            RadioGroupContent(
                radioOptions = radioOptions,
                defaultSelection = defaultSelection,
                onItemClick = { item -> onItemClick(item) }
            )
        }
    }
}