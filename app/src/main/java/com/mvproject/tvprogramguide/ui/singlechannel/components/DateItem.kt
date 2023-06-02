package com.mvproject.tvprogramguide.ui.singlechannel.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun DateItem(date: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(MaterialTheme.dimens.size8),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Outlined.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            Text(
                text = date,
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
fun DateItemView() {
    TvGuideTheme() {
        DateItem("22.03.2012")
    }
}

@Preview
@Composable
fun DateItemViewDark() {
    TvGuideTheme(darkTheme = true) {
        DateItem("22.03.2012")
    }
}
