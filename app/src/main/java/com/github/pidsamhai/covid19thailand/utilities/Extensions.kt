package com.github.pidsamhai.covid19thailand.utilities

fun Int?.toCurrency(): String {
    return if (this == null) ""
    else "%,d".format(this)
}