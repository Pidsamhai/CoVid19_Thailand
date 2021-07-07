package com.github.pidsamhai.covid19thailand.ui.today

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.ui.widget.CardItem
import com.github.pidsamhai.covid19thailand.ui.widget.CardItemDefault
import com.github.pidsamhai.covid19thailand.utilities.toCurrency
import org.koin.androidx.compose.getViewModel

@Composable
fun TodayPage(
    viewModel: ToDayViewModel = getViewModel(),
    subtitleCallback: SubtitleCallback
) {
    val today by viewModel.today.observeAsState()
    subtitleCallback(today?.updateDate)

    TodayPageContent(
        today = today
    )
}

@Composable
private fun TodayPageContent(
    today: Today? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardItem(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9),
            title = "ติดเชื่อสะสม",
            value = today?.confirmed.toCurrency(),
            increment = today?.newConfirmed.toCurrency(),
            backgroundColor = colorResource(id = R.color.confirmed),
            textStyle = CardItemDefault.textStyle.copy(fontSize = 16.sp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardItem(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(9f / 16),
                title = "หายแล้ว",
                value = today?.recovered.toCurrency(),
                increment = today?.newRecovered.toCurrency(),
                backgroundColor = colorResource(id = R.color.recovered)
            )

            CardItem(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(9f / 16),
                title = "รักษาอยู่ใน รพ.",
                value = today?.hospitalized.toCurrency(),
                increment = today?.newHospitalized.toCurrency(),
                backgroundColor = colorResource(id = R.color.hospitalized)
            )

            CardItem(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(9f / 16),
                title = "เสียชีวิต",
                value = today?.deaths.toCurrency(),
                increment = today?.newDeaths.toCurrency(),
                backgroundColor = colorResource(id = R.color.deaths)
            )
        }
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