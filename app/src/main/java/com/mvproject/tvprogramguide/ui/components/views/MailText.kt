package com.mvproject.tvprogramguide.ui.components.views

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun MailText(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val emailIntent = remember {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("mvproject@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback for TV Program Guide")
        }
    }

    val annotatedString = buildAnnotatedString {
        append("mailto:")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("mvproject")
        }
    }
    Text(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                context.startActivity(emailIntent)
            },
        text = annotatedString,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
    )
}