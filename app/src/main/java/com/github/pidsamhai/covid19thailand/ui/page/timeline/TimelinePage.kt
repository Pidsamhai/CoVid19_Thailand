package com.github.pidsamhai.covid19thailand.ui.page.timeline

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.Data
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.toDataSet
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.viewmodel.TimeLineViewModel
import com.github.pidsamhai.covid19thailand.ui.widget.ChartWidget
import com.github.pidsamhai.covid19thailand.utilities.chartLabelColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.getViewModel

@Composable
fun TimelinePage(
    viewModel: TimeLineViewModel = getViewModel(),
    subtitleCallback: SubtitleCallback
) {
    var chartModel by remember { mutableStateOf<AAChartModel?>(null) }
    val dataResult by viewModel.datas.observeAsState()
    val timeLineResult by viewModel.timeline.observeAsState(initial = Result.Initial)
    var timeLine by remember { mutableStateOf<TimeLine?>(null) }
    if (timeLineResult is Result.Success) timeLine =
        (timeLineResult as Result.Success<TimeLine>).data
    if (dataResult is Result.Success) {
        val dataSet = (dataResult as Result.Success<List<Data>>).data
        chartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .yAxisTitle(" ")
            .backgroundColor("#00000000")
            .axesTextColor(chartLabelColor)
            .markerSymbol(AAChartSymbolType.Circle)
            .markerRadius(3f)
            .dataLabelsEnabled(true)
            .dataLabelsStyle(AAStyle())
            .categories(dataSet.map { it.date }.distinct().toTypedArray())
            .series(
                dataSet.toDataSet().map {
                    AASeriesElement()
                        .name(it.title)
                        .data(it.data.toTypedArray())
                        .color(it.color)
                }.toTypedArray()
            )
    }
    val isLoading = timeLineResult is Result.Loading || dataResult is Result.Loading

    subtitleCallback(timeLine?.updateDate)

    val swipeState = rememberSwipeRefreshState(isRefreshing = isLoading)

    SwipeRefresh(
        state = swipeState,
        onRefresh = { }
    ) {
        TimelinePageContent(
            model = chartModel
        )
    }
}

@Composable
private fun TimelinePageContent(
    isPreview: Boolean = false,
    model: AAChartModel? = null
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ChartWidget(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4 / 3f)
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