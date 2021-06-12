package com.github.pidsamhai.covid19thailand.utilities

fun Int?.toCurrency(): String = "%,d".format(this)