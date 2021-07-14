package com.github.pidsamhai.covid19thailand.ui.today

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.ui.widget.CardItem
import com.github.pidsamhai.covid19thailand.ui.widget.CardItemDefault
import com.github.pidsamhai.covid19thailand.ui.widget.ReportWidget
import com.github.pidsamhai.covid19thailand.utilities.toCurrency
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.getStateViewModel
import org.koin.androidx.compose.getViewModel

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
            confirmed = today?.confirmed,
            newConfirmed = today?.newConfirmed,
            recovered = today?.recovered,
            newRecovered = today?.newRecovered,
            hospitalized = today?.hospitalized,
            newHospitalized = today?.newHospitalized,
            deaths = today?.deaths,
            newDeaths = today?.newDeaths
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodayPagePreview() {
    TodayPageContent(
        today = Today(
            confirmed = 100,
            newConfirmed = 20,
            deaths = 150,
            newDeaths = 1,
            recovered = 10,
            newRecovered = 1,
            hospitalized = 70,
            newHospitalized = 7,
            updateDate = "10/10/2021",
            devBy = "FuckOff"
        )
    )
}