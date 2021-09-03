package com.github.pidsamhai.covid19thailand.utilities

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.pidsamhai.covid19thailand.R
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber

const val DATABASE_NAME = "covid19-db"
const val OWNER = " Pidsamhai"
const val REPOSITORY = "CoVid19_Thailand"

val StatusColors = listOf("#FFFF00", "#FF0000", "#00FF00")
val StatusTexts = listOf("Confirmed", "Death", "Recovered")

val DDC_DATE_PATTERN = "^[\\d]{4}[-][\\d]{2}[-][\\d]{2}[\\s].*\$".toRegex()

fun ddcDateReformat(string: String): String {
    val date = string.split(" ")
    Timber.d("${date.first()}T${date.last()}")
    return "${date.first()}T${date.last()}"
}

const val DEFAULT_LAST_UPDATE_TEMPLATE = "( อัพเดทล่าสุด %s/%s/%s %s:%s:%s )"

fun String?.toLastUpdate(template: String = DEFAULT_LAST_UPDATE_TEMPLATE): String? {
    var datetime: LocalDateTime? = null
    if (this != null) {
        datetime = if (this.matches(DDC_DATE_PATTERN)) {
            ddcDateReformat(this).toLocalDateTime()
        } else {
            /**
             * Force remove TimeZone
             * TimeZone not work
             * 2020-06-02T20:45:06+00:00
             */
            this.split("+")[0].toLocalDateTime()
        }
    }
    return if (datetime != null) template.format(
        datetime.dayOfMonth,
        datetime.monthNumber,
        datetime.year,
        datetime.hour,
        datetime.minute,
        datetime.second
    ) else datetime
}

fun updateTime(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return "อัพเดทล่าสุด %s/%s/%s %s:%s:%s".format(
        now.dayOfMonth,
        now.monthNumber,
        now.year,
        now.hour,
        now.minute,
        now.second
    )
}

object Keys {
    init {
        System.loadLibrary("native-lib")
    }

    external fun rapidCovid19Api(): String
}

val chartLabelColor: String
    @Composable get() = if (isSystemInDarkTheme()) "#ffffff" else "#000000"

val TOTAL_LABEL_FORMAT: String
 @Composable get() = stringResource(id = R.string.label_total)
