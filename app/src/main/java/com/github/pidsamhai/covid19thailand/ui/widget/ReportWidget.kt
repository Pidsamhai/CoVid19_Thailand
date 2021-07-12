package com.github.pidsamhai.covid19thailand.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.utilities.toCurrency

@Composable
fun ReportWidget(
    modifier: Modifier = Modifier,
    confirmed: Int?,
    newConfirmed: Int?,
    recovered: Int?,
    newRecovered: Int?,
    hospitalized: Int?,
    newHospitalized: Int?,
    deaths: Int?,
    newDeaths: Int?
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardItem(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9),
            title = "ติดเชื่อสะสม",
            value = confirmed.toCurrency(),
            increment = newConfirmed?.toCurrency(),
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
                value = recovered.toCurrency(),
                increment = newRecovered?.toCurrency(),
                backgroundColor = colorResource(id = R.color.recovered)
            )

            CardItem(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(9f / 16),
                title = "รักษาอยู่ใน รพ.",
                value = hospitalized.toCurrency(),
                increment = newHospitalized?.toCurrency(),
                backgroundColor = colorResource(id = R.color.hospitalized)
            )

            CardItem(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(9f / 16),
                title = "เสียชีวิต",
                value = deaths.toCurrency(),
                increment = newDeaths?.toCurrency(),
                backgroundColor = colorResource(id = R.color.deaths)
            )
        }
    }
}

@Preview
@Composable
private fun ReportWidgetPreview() {
    ReportWidget(
        confirmed = 100,
        newConfirmed = null,
        recovered = 100,
        newRecovered = 100,
        hospitalized = 10,
        newHospitalized = 100,
        deaths = 3000,
        newDeaths = 0
    )
}