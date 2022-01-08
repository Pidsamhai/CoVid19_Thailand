package com.github.pidsamhai.covid19thailand.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomAppBar(
    title: String,
    subtitle: String? = null
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background
    ) {
        Spacer(Modifier.width(12.dp))
        Column {
            ProvideTextStyle(
                value = MaterialTheme
                    .typography
                    .h6
                    .copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
            ) {
                Text(
                    modifier = Modifier.padding(0.dp),
                    text = title
                )
            }
            ProvideTextStyle(value = MaterialTheme.typography.subtitle2.copy(fontSize = 12.sp)) {
                subtitle?.let {
                    Text(text = it)
                }
            }
        }
    }
}

@Preview
@Composable
private fun CustomAppBarPreView() {
    CustomAppBar(
        title = "Today",
        subtitle = "10/10/2021"
    )
}