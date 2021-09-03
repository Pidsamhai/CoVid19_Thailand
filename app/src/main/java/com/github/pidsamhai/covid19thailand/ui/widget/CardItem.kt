package com.github.pidsamhai.covid19thailand.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.pidsamhai.covid19thailand.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.github.pidsamhai.covid19thailand.ui.theme.AppTheme
import org.koin.androidx.compose.get

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    increment: String?,
    backgroundColor: Color,
    textStyle: TextStyle = CardItemDefault.textStyle
) {
    Card(
        modifier = modifier,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompositionLocalProvider(LocalTextStyle provides textStyle) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                increment?.let {
                    Text(
                        text = it,
                        fontSize = 25.sp
                    )
                }
                Text(
                    text = value,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

object CardItemDefault {
    val textStyle
        @Composable get() = MaterialTheme.typography.body2
        .copy(
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 16.sp
        )
}

@Preview(showBackground = true)
@Composable
private fun CardItemPreView() {
    AppTheme {
        CardItem(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9),
            title = "ติดเชื่อสะสม",
            value = "สะสม 199,999",
            increment = "300",
            backgroundColor = colorResource(id = R.color.confirmed)
        )
    }
}