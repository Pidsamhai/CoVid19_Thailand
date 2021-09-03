package com.github.pidsamhai.covid19thailand.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.utilities.TOTAL_LABEL_FORMAT
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
    newDeaths: Int?,
    textStyle: TextStyle = CardItemDefault.textStyle,
    newConfirmStyle: TextStyle = CardItemDefault.textStyle,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardItem(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9),
            title = stringResource(id = R.string.label_new_confirm),
            value = confirmed.toCurrency(TOTAL_LABEL_FORMAT),
            increment = newConfirmed?.toCurrency(),
            backgroundColor = colorResource(id = R.color.confirmed),
            textStyle = newConfirmStyle
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recovered?.let {
                CardItem(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    title = stringResource(id = R.string.label_new_recovery),
                    value = recovered.toCurrency(TOTAL_LABEL_FORMAT),
                    increment = newRecovered?.toCurrency(),
                    backgroundColor = colorResource(id = R.color.recovered),
                    textStyle = textStyle
                )
            }

            hospitalized?.let {
                CardItem(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    title = "รักษาอยู่ใน รพ.",
                    value = hospitalized.toCurrency(),
                    increment = newHospitalized?.toCurrency(),
                    backgroundColor = colorResource(id = R.color.hospitalized),
                    textStyle = textStyle
                )
            }

            deaths?.let {
                CardItem(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    title = stringResource(id = R.string.label_new_death),
                    value = deaths.toCurrency(TOTAL_LABEL_FORMAT),
                    increment = newDeaths?.toCurrency(),
                    backgroundColor = colorResource(id = R.color.deaths),
                    textStyle = textStyle
                )
            }
        }
    }
}

@Preview
@Composable
private fun ReportWidgetPreview() {
    ReportWidget(
        confirmed = 100,
        newConfirmed = 10,
        recovered = 100,
        newRecovered = 100,
        hospitalized = 10,
        newHospitalized = 100,
        deaths = 3000,
        newDeaths = 0
    )
}