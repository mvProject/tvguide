package com.mvproject.tvprogramguide.ui.components.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO

@Composable
fun RadioGroupScrollable(
    radioOptions: List<String> = listOf(),
    defaultSelection: Int = COUNT_ZERO,
    onItemClick: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.size220)
            .verticalScroll(
                state = scrollState,
                enabled = true,
            ),
    ) {
        RadioGroupContent(
            radioOptions = radioOptions,
            defaultSelection = defaultSelection,
            onItemClick = { item -> onItemClick(item) },
        )
    }
}
