package com.mvproject.tvprogramguide.ui.settings.channels

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvproject.tvprogramguide.components.pickers.NumberPicker

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
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(6f)
                    .padding(8.dp),
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis
            )

            NumberPicker(
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
fun PickerItemPreview(){
    PickerItem("Available channels update period", 10, onValueSelected = {})
}

