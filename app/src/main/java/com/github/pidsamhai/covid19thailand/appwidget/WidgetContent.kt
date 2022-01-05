package com.github.pidsamhai.covid19thailand.appwidget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.ui.MainActivity
import com.github.pidsamhai.covid19thailand.utilities.*

@Composable
fun ContentWidget(static: Static?) {
    return ContentWidget(
        prefix = "World: ",
        title = static?.static?.country,
        newCase = static?.static?.cases?.new?.toInt(),
        totalCase = static?.static?.cases?.total,
        newDeath = static?.static?.deaths?.new?.toInt(),
        totalDeath = static?.static?.deaths?.total,
        updateDate = static?.static?.time,
        manualUpdateAction = actionRunCallback<ManualUpdateWidgetCallBack>(),
        openConfigAction = { context, glanceId ->
            actionStartConfigureActivity<WorldWidgetConfigureActivity>(
                glanceId,
                context
            )
        }
    )
}

@Composable
fun ContentWidget(data: TodayByProvince?) {
    return ContentWidget(
        prefix = "",
        title = data?.province,
        newCase = data?.newCase,
        totalCase = data?.totalCase,
        newDeath = data?.newDeath,
        totalDeath = data?.totalDeath,
        updateDate = data?.updateDate,
        manualUpdateAction = actionRunCallback<ManualUpdateWidgetCallBack>(),
        openConfigAction = { context, glanceId ->
            actionStartConfigureActivity<ThaiWidgetConfigureActivity>(
                glanceId,
                context
            )
        }
    )
}

@Composable
fun ContentWidget(today: Today?) {
    return ContentWidget(
        prefix = "",
        title = "ทั้งประเทศ",
        newCase = today?.newCase,
        totalCase = today?.totalCase,
        newDeath = today?.newDeath,
        totalDeath = today?.totalDeath,
        updateDate = today?.updateDate,
        manualUpdateAction = actionRunCallback<ManualUpdateWidgetCallBack>(),
        openConfigAction = { context, glanceId ->
            actionStartConfigureActivity<ThaiWidgetConfigureActivity>(
                glanceId,
                context
            )
        }
    )
}

@Composable
private fun ContentWidget(
    prefix: String,
    title: String?,
    newCase: Int?,
    totalCase: Int?,
    newDeath: Int?,
    totalDeath: Int?,
    updateDate: String?,
    openConfigAction: (Context, GlanceId) -> Action,
    manualUpdateAction: Action
) {
    val context = LocalContext.current
    val glanceId = LocalGlanceId.current
    Column(
        modifier = GlanceModifier
            .clickable(
                actionStartActivity(
                    Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
            )
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = prefix, style = TextStyle(fontWeight = FontWeight.Bold))
            Text(text = title ?: "")
            Spacer(
                modifier = GlanceModifier.defaultWeight()
            )
            Text(text = updateDate.toLastUpdate(APPWIDGET_LAST_UPDATE_TEMPLATE) ?: "")
            Spacer(
                modifier = GlanceModifier.width(8.dp)
            )
            Image(
                modifier = GlanceModifier.clickable(openConfigAction(context, glanceId)),
                provider = ImageProvider(R.drawable.ic_round_settings_24),
                contentDescription = "Setting Icon"
            )
        }
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight()
        ) {
            Column(
                modifier = GlanceModifier
                    .defaultWeight()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = context.getString(R.string.label_new_confirm),
                    style = TextStyle(
                        color = ColorProvider(R.color.new_case),
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.height(16.dp))
                Text(
                    text = newCase.toCurrency(),
                    style = TextStyle(
                        color = ColorProvider(R.color.new_case),
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.height(16.dp))
                Text(
                    text = totalCase.toCurrency(APPWIDGET_TOTAL_TEMPLATE),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = ColorProvider(R.color.new_case),
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Column(
                modifier = GlanceModifier
                    .defaultWeight()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = context.getString(R.string.label_new_death),
                    style = TextStyle(
                        color = ColorProvider(R.color.deaths),
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.height(16.dp))
                Text(
                    text = newDeath.toCurrency(),
                    style = TextStyle(
                        color = ColorProvider(R.color.deaths),
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.height(16.dp))
                Text(
                    text = totalDeath.toCurrency(APPWIDGET_TOTAL_TEMPLATE),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = ColorProvider(R.color.deaths),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Row(
            modifier = GlanceModifier.fillMaxWidth()
        ) {
            Text(text = updateTime())
            Spacer(modifier = GlanceModifier.defaultWeight())
            Image(
                modifier = GlanceModifier.clickable(manualUpdateAction),
                provider = ImageProvider(R.drawable.ic_round_refresh_24),
                contentDescription = "Refresh Icon"
            )
        }
    }
}

@Composable
fun EmptyContent() {
    Row(
        modifier = GlanceModifier
            .clickable(
                actionRunCallback<ManualUpdateWidgetCallBack>()
            )
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            provider = ImageProvider(R.drawable.ic_round_refresh_error_24),
            contentDescription = "Refresh Icon"
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Text(
            text = "No data",
            style = TextStyle(
                color = ColorProvider(Color.Red),
                fontWeight = FontWeight.Medium
            )
        )
    }
}