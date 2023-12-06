package com.mvproject.tvprogramguide.ui.components.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO

@Composable
fun RadioGroup(
    radioOptions: List<String> = listOf(),
    defaultSelection: Int = COUNT_ZERO,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(all = MaterialTheme.dimens.size2)
    ) {
        RadioGroupContent(
            radioOptions = radioOptions,
            defaultSelection = defaultSelection,
            onItemClick = { item -> onItemClick(item) }
        )
    }
}
