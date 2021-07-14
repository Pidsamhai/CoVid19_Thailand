package com.github.pidsamhai.covid19thailand

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.pidsamhai.covid19thailand.ui.update.UpdateDialog
import com.github.pidsamhai.covid19thailand.ui.viewmodel.AboutViewModel
import org.koin.androidx.compose.getViewModel

@Preview(showBackground = true)
@Composable
fun AboutPage(
    openUpdateDialog: () -> Unit = { }
) {
    var showUpdateDialog by remember { mutableStateOf(false) }

    if (showUpdateDialog) {
        UpdateDialog(
            showDialog = showUpdateDialog,
            onDismiss = { showUpdateDialog = false }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)) {
            Text(text = "Special thank all api")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(id = R.drawable.ic_logo_ddc),
                    contentDescription = null
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_rapid_logo),
                    contentDescription = null
                )
            }

            Text(text = "Font  IBM Plex®")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AndroidView(
                    modifier = Modifier.size(60.dp),
                    factory = { ImageView(it) }
                ) {
                    it.setImageResource(R.mipmap.ic_launcher)
                }

                Text(text = "Copy right Pidsamhai 2020")
            }

            Button(onClick = { openUpdateDialog() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_update),
                    contentDescription = null
                )
                Text(text = stringResource(id = R.string.check_update).uppercase())
            }

            Button(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_forever),
                    contentDescription = null
                )
                Text(text = stringResource(id = R.string.clean_cache).uppercase())
            }
        }
    }
}