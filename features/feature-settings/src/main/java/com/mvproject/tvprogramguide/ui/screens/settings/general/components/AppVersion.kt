package com.mvproject.tvprogramguide.ui.screens.settings.general.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tvprogramguide.utils.AppConstants.empty

@Composable
fun AppVersion(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appVersion = remember(context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: String.empty
        } catch (e: Exception) {
            String.empty
        }
    }
    if (appVersion.isNotBlank()) {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = "current: $appVersion",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }

}
