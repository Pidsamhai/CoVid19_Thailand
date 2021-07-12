package com.github.pidsamhai.covid19thailand.ui.widget

import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView

@Composable
fun ChartWidget(
    modifier: Modifier = Modifier,
    isPreview: Boolean = false,
    model: AAChartModel? = null
) {
    val context = LocalContext.current

    val view = remember {
        AAChartView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    if (!isPreview) {
        AndroidView(
            modifier = modifier,
            factory = { view },
            update = { v ->
                model?.let {
                    v.aa_drawChartWithChartModel(it)
                }
            }
        )
    }
    else Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Chart")
    }
}