package com.github.pidsamhai.covid19thailand.ui.widget

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon

@Preview(showBackground = true)
@Composable
fun MarkwonWidget(
    modifier: Modifier = Modifier,
    text: String = ""
) {
    val context = LocalContext.current
    val markwon = remember { Markwon.create(context) }

    AndroidView(
        factory = { TextView(context) },
        update = {
            markwon.setMarkdown(it, text)
        }
    )
}