package com.mvproject.tvprogramguide.ui.onboard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.utils.AppConstants
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun AnimatedCompleteButton(
    modifier: Modifier,
    currentPage: Int = AppConstants.ONBOARD_LAST_PAGES_INDEX,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(horizontal = MaterialTheme.dimens.size40),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = currentPage == AppConstants.ONBOARD_LAST_PAGES_INDEX
        ) {
            ElevatedButton(
                onClick = onClick,
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size8)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(id = R.string.onboard_btn_complete),
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
