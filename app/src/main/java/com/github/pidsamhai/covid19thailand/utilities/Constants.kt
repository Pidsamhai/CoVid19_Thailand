package com.github.pidsamhai.covid19thailand.utilities

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import kotlinx.datetime.toLocalDateTime

const val DATABASE_NAME = "covid19-db"
const val OWNER = " Pidsamhai"
const val REPOSITORY = "CoVid19_Thailand"

val StatusColors = listOf("#FFFF00", "#FF0000", "#00FF00")
val StatusTexts = listOf("Confirmed", "Death", "Recovered")

val DDC_DATE_PATTERN = "^[\\d]{2}[/][\\d]{2}[/][\\d]{4}[\\s][\\d]{2}[:][\\d]{2}\$".toRegex()

fun ddcDateReformat(string: String): String {
    val date = string.split(" ").first().split("/").reversed().joinToString("-")
    val time = (string.split(" ").lastOrNull() ?: "00:00") + ":00"
    return "${date}T$time"
}

fun String?.toLastUpdate(): String? {
    var datetime: String? = null
    if (this != null) {
        val parserDate = if (this.matches(DDC_DATE_PATTERN)) {
            ddcDateReformat(this).toLocalDateTime()
        } else {
            /**
             * Force remove TimeZone
             * TimeZone not work
             */
            this.split("+")[0].toLocalDateTime()
        }
        datetime = "${parserDate.dayOfMonth}/${parserDate.monthNumber + 1}/${parserDate.year} " +
                "${parserDate.hour}:${parserDate.minute}"
    }
    return if (datetime != null) "( อัพเดทล่าสุด $datetime )" else datetime
}

object Keys {
    init {
        System.loadLibrary("native-lib")
    }

    external fun rapidCovid19Api(): String
}

val chartLabelColor: String
    @Composable get() = if (isSystemInDarkTheme()) "#ffffff" else "#000000"
