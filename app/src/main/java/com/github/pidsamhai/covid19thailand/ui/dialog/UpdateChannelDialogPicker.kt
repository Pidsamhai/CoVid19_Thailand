package com.github.pidsamhai.covid19thailand.ui.dialog

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.edit
import org.koin.androidx.compose.get
import org.koin.core.qualifier.named

const val UPDATE_CHANNEL_KEY = "UPDATE_CHANNEL_KEY"

@Composable
fun UpdateChannelDialogPicker(
    showDialog: Boolean = false,
    onDismiss: () -> Unit,
    pref: SharedPreferences = get(named("defaultPref"))
) {
    val saveConfig: (Boolean) -> Unit = {
        pref.edit {
            putBoolean(UPDATE_CHANNEL_KEY, it)
        }
        onDismiss()
    }

    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            DialogContentContent(
                isDevChannel = pref.getBoolean(UPDATE_CHANNEL_KEY, false),
                onDismiss = onDismiss,
                onSave = saveConfig
            )
        }
    }
}

@Preview
@Composable
private fun DialogContentContent(
    isDevChannel: Boolean = false,
    onDismiss: () -> Unit = { },
    onSave: (Boolean) -> Unit = { }
) {
    var isDevChannelValue by remember {
        mutableStateOf(isDevChannel)
    }
    Card(
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(text = "Select update channel", style = MaterialTheme.typography.h6)
            Column(
                modifier = Modifier.padding(
                    vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = !isDevChannelValue,
                        onClick = { isDevChannelValue = false })
                    Text(text = "Stable")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = isDevChannelValue,
                        onClick = { isDevChannelValue = true })
                    Text(text = "Dev")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = "CLOSE")
                }
                TextButton(onClick = { onSave(isDevChannelValue) }) {
                    Text(text = "SAVE")
                }
            }
        }
    }
}