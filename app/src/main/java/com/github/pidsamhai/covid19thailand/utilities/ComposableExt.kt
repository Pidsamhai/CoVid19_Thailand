package com.github.pidsamhai.covid19thailand.utilities

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable fun getString(
    @StringRes resId: Int
): String = LocalContext.current.resources.getString(resId)