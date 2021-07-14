package com.github.pidsamhai.covid19thailand.ui.worldwide

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.pidsamhai.covid19thailand.Result
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.dialog.SelectCountryDialog
import com.github.pidsamhai.covid19thailand.ui.viewmodel.WorldWideModel
import com.github.pidsamhai.covid19thailand.ui.widget.ReportWidget
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.getStateViewModel

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun WorldWidePage(
    viewModel: WorldWideModel = getStateViewModel(),
    subtitleCallback: SubtitleCallback = { }
) {
    val countriesResult by viewModel.countriesX.observeAsState(initial = Result.Initial)
    val staticResult by viewModel.static.observeAsState(initial = Result.Initial)

    var showState by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("Please Select area...") }
    var countries by remember { mutableStateOf(listOf<String>()) }
    var static by remember { mutableStateOf<Static?>(null) }
    val isLoading = countriesResult is Result.Loading
    val swipeState = rememberSwipeRefreshState(isRefreshing = isLoading)

    val onSelectedCountry: (country: String) -> Unit = {
        viewModel.getCountry(it)
        selectedCountry = it
    }

    when (countriesResult) {
        is Result.Success -> countries = (countriesResult as Result.Success<List<String>>).data
    }

    when (staticResult) {
        is Result.Success -> {
            static = (staticResult as Result.Success<Static>).data
            subtitleCallback(static?.datas?.firstOrNull()?.time)
        }
    }

    SelectCountryDialog(
        title = "Select Country",
        selected = { onSelectedCountry(it) },
        showDialog = showState,
        onDismiss = { showState = false },
        items = countries
    )

    SwipeRefresh(
        state = swipeState,
        onRefresh = { viewModel.refresh() }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedCountry)
                IconButton(
                    onClick = { showState = true }
                ) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            static?.datas?.firstOrNull()?.let {
                ReportWidget(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    confirmed = it.cases?.total,
                    newConfirmed = it.cases?.new?.toIntOrNull(),
                    recovered = it.cases?.recovered,
                    newRecovered = null,
                    hospitalized = it.cases?.active,
                    newHospitalized = null,
                    deaths = it.deaths?.total,
                    newDeaths = it.deaths?.new?.toIntOrNull()
                )
            }
        }
    }
}