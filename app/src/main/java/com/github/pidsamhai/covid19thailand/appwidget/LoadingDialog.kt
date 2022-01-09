package com.github.pidsamhai.covid19thailand.appwidget

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.github.pidsamhai.covid19thailand.R

@Suppress("FunctionName")
fun LoadingDialog(context: Context) = AlertDialog.Builder(context)
    .setView(R.layout.dialog_loading)
    .setCancelable(false)
    .create()