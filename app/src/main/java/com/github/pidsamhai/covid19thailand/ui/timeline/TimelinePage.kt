package com.github.pidsamhai.covid19thailand.ui.timeline

import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.pidsamhai.covid19thailand.network.response.ddc.toLineDataSet
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.viewmodel.TimeLineViewModel
import com.github.pidsamhai.covid19thailand.utilities.toLastUpdate

import org.koin.androidx.compose.getViewModel

@Composable
fun TimelinePage(
    viewModel: TimeLineViewModel = getViewModel(),
    subtitleCallback: SubtitleCallback
) {
    val datas by viewModel.datas.observeAsState()
    val dataSet = datas?.reversed()?.toLineDataSet()
    val timeLine by viewModel.timeline.observeAsState()
    val status = listOf("Confirmed", "Death", "Recovered")
    val colors = listOf("#FFFF00", "#FF0000", "#00FF00")
    val aaChartModel: AAChartModel = AAChartModel()
        .chartType(AAChartType.Line)
        .yAxisTitle(" ")
        .dataLabelsEnabled(true)
        .categories(dataSet?.date?.map { it.toString() }?.toTypedArray() ?: arrayOf())
        .series(
            datas?.let {
                dataSet?.toList()?.mapIndexed { index, data ->
                    AASeriesElement()
                        .name(status[index])
                        .data(data.toTypedArray())
                        .color(colors[index])
                }
            }?.toTypedArray() ?: arrayOf()
        )
    timeLine?.updateDate?.let {
        subtitleCallback(it)
    }

    TimelinePageContent(
        model = aaChartModel
    )
}

@Composable
private fun TimelinePageContent(
    isPreview: Boolean = false,
    model: AAChartModel? = null
) {
    Column {
        ChartWidget(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp),
            isPreview = isPreview,
            model = model
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimelinePagePreView() {
    TimelinePageContent(
        isPreview = true
    )
}

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

    if (!isPreview)
        AndroidView(
            modifier = modifier,
            factory = { view }
        ) {
            model?.let {
                view.aa_drawChartWithChartModel(model)
            }
        }
    else Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Chart")
    }
}