package com.mvproject.tvprogramguide.ui.components.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO

@Composable
fun RadioGroupScrollable(
    radioOptions: List<String> = listOf(),
    defaultSelection: Int = COUNT_ZERO,
    onItemClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .background(MaterialTheme.colors.primary)
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
