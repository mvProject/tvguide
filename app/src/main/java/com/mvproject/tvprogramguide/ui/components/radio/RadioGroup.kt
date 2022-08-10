package com.mvproject.tvprogramguide.ui.components.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun RadioGroup(
    radioOptions: List<String> = listOf(),
    title: String = NO_VALUE_STRING,
    defaultSelection: Int = COUNT_ZERO,
    onItemClick: (String) -> Unit
) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .padding(MaterialTheme.dimens.size8)
            .fillMaxWidth(),
        elevation = MaterialTheme.dimens.size8,
        shape = RoundedCornerShape(MaterialTheme.dimens.size8),
    ) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.dimens.size2)
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(MaterialTheme.dimens.size6),
                style = MaterialTheme.appTypography.textBold
            )

            RadioGroupContent(
                radioOptions = radioOptions,
                defaultSelection = defaultSelection,
                onItemClick = { item -> onItemClick(item) }
            )
        }
    }
}
