package com.mvproject.tvprogramguide.ui.screens.settings.app.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.ui.components.radio.RadioGroupContent
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RadioGroup(
    radioOptions: ImmutableList<String> = persistentListOf(),
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
            onItemClick = onItemClick
        )
    }
}
