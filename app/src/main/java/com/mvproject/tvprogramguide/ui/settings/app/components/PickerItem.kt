package com.mvproject.tvprogramguide.ui.settings.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.components.pickers.NumberPicker

@Composable
fun PickerItem(
    title: String,
    initialValue: Int = 0,
    min: Int = 0,
    max: Int = 10,
    onValueSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        // .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight4)
                    .padding(MaterialTheme.dimens.size8),
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            NumberPicker(
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight2),
                default = initialValue,
                min = min,
                max = max,
            ) { value ->
                onValueSelected(value)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PickerItemPreview() {
    TvGuideTheme() {
        Column() {
            PickerItem("Available channels update period", 1, onValueSelected = {})
            PickerItem("Available channels update period", 5, onValueSelected = {})
            PickerItem("Available channels update period", 7, onValueSelected = {})
            PickerItem("Available channels update period", 10, onValueSelected = {})
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PickerItemDarkPreview() {
    TvGuideTheme(darkTheme = true) {
        Column() {
            PickerItem("Available channels update period", 1, onValueSelected = {})
            PickerItem("Available channels update period", 5, onValueSelected = {})
            PickerItem("Available channels update period", 7, onValueSelected = {})
            PickerItem("Available channels update period", 10, onValueSelected = {})
        }
    }
}
