package com.mvproject.tvprogramguide.ui.components.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tvprogramguide.utils.AppConstants.empty

@Composable
fun AppVersion(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appVersion = try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (e: Exception) {
        String.empty
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