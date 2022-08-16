package com.mvproject.tvprogramguide.ui.singlechannel.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun DateItem(date: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colors.surface),
        shape = RoundedCornerShape(MaterialTheme.dimens.size4),
        elevation = MaterialTheme.dimens.size2
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
                tint = MaterialTheme.colors.onPrimary
            )

            Spacer(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.dimens.size8
                )
            )

            Text(
                text = date,
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1),
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.dimens.font16,
                style = MaterialTheme.appTypography.textSemiBold
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
    TvGuideTheme(true) {
        DateItem("22.03.2012")
    }
}
