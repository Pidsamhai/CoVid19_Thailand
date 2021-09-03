package com.github.pidsamhai.covid19thailand.utilities

fun Int?.toCurrency(template: String = "%,d"): String {
    return if (this == null) ""
    else template.format(this)
}