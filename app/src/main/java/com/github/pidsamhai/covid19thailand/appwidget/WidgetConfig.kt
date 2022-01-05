package com.github.pidsamhai.covid19thailand.appwidget

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WidgetConfig(
    @SerializedName("location")
    val location: String,
    @SerializedName("type")
    val type: String
)
