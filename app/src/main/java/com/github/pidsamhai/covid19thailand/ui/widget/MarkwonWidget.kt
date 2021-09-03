package com.github.pidsamhai.covid19thailand.ui.widget

import android.widget.TextView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon

private val DEFAULT_TEXT_COLOR: Color
    @Composable get() = if (isSystemInDarkTheme()) Color.White else Color.Black

@Preview(showBackground = true)
@Composable
fun MarkwonWidget(
    modifier: Modifier = Modifier,
    text: String = "",
    textColor: Color = DEFAULT_TEXT_COLOR
) {
    val context = LocalContext.current
    val markwon = remember { Markwon.create(context) }

    AndroidView(
        factory = {
            TextView(context).apply {
                setTextColor(textColor.toArgb())
            }
        },
        update = {
            markwon.setMarkdown(it, text)
        }
    )
}