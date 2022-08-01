package com.mvproject.tvprogramguide.components.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RadioGroup(
    radioOptions: List<String> = listOf(),
    title: String = "",
    defaultSelection: Int = 0,
    onItemClick: (String) -> Unit
) {
    if (radioOptions.isNotEmpty()) {
        Card(
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                Modifier.padding(10.dp)
            ) {
                Text(
                    text = title,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp),
                )

                RadioGroupContent(
                    radioOptions = radioOptions,
                    defaultSelection = defaultSelection,
                    onItemClick = { item -> onItemClick(item) }
                )
            }
        }
    }
}
