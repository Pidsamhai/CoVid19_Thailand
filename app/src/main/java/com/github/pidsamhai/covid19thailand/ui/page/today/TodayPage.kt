package com.github.pidsamhai.covid19thailand.ui.page.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.ui.widget.ReportWidget
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getStateViewModel

@Composable
fun TodayPage(
    viewModel: ToDayViewModel = getStateViewModel(),
    subtitleCallback: SubtitleCallback,
) {
    val todayResult by viewModel.today.observeAsState(initial = Result.Initial)
    val isLoading = todayResult is Result.Loading
    var today: Today? by remember { mutableStateOf(null) }

    if (todayResult is Result.Success) {
        today = (todayResult as Result.Success).data
    }

    val swipeState = rememberSwipeRefreshState(isRefreshing = isLoading)

    subtitleCallback(today?.updateDate)

    LaunchedEffect(key1 = Unit) {
        viewModel.refreshNotification()
    }

    SwipeRefresh(
        state = swipeState,
        onRefresh = { viewModel.refresh() }
    ) {
        TodayPageContent(
            today = today,
            notification = viewModel.notification
        )
    }

}

@Composable
private fun TodayPageContent(
    today: Today? = null,
    notification: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (notification.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.Red)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = notification,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.body1
                )
            }
        }
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
        notification = """
            Jumble each side of the chocolate with four and a half teaspoons of eggs.
        """.trimIndent(),
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