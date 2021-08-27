package com.github.pidsamhai.covid19thailand.ui.page.today

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.ui.widget.ReportWidget
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.getStateViewModel

@Composable
fun TodayPage(
    viewModel: ToDayViewModel = getStateViewModel(),
    subtitleCallback: SubtitleCallback
) {
    val todayResult by viewModel.today.observeAsState(initial = Result.Initial)
    val isLoading = todayResult is Result.Loading
    var today: Today? by remember { mutableStateOf(null) }

    if (todayResult is Result.Success) {
        today = (todayResult as Result.Success).data
    }

    val swipeState = rememberSwipeRefreshState(isRefreshing = isLoading)

    subtitleCallback(today?.updateDate)

    SwipeRefresh(
        state = swipeState,
        onRefresh = { viewModel.refresh() }
    ) {
        TodayPageContent(
            today = today
        )
    }

}

@Composable
private fun TodayPageContent(
    today: Today? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReportWidget(
            confirmed = today?.totalCase,
            newConfirmed = today?.newCase,
            recovered = null,
            newRecovered = null,
            hospitalized = null,
            newHospitalized = null,
            deaths = null,
            newDeaths = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodayPagePreview() {
    TodayPageContent(
        today = Today(
            newCase = 100,
            newCaseExcludeAbroad = 20,
            totalCase = 150,
            totalCaseExcludeAbroad = 1,
            updateDate = "10/10/2021",
            txnDate = "10/10/2021"
        )
    )
}