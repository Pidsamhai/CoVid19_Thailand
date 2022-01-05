package com.github.pidsamhai.covid19thailand.ui.page.about

import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.pidsamhai.covid19thailand.BuildConfig
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.ui.page.update.UpdateDialog
import com.github.pidsamhai.covid19thailand.ui.viewmodel.AboutPageVM
import org.koin.androidx.compose.getViewModel

@Composable
fun AboutPage(
    openUpdateDialog: () -> Unit = { },
    viewModel: AboutPageVM = getViewModel()
) {
    val context = LocalContext.current
    AboutPageContent(
        openUpdateDialog = openUpdateDialog,
        clearDatabase = {
            viewModel.clearDatabase {
                Toast.makeText(context, "Cleared", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AboutPageContent(
    openUpdateDialog: () -> Unit = { },
    clearDatabase: () -> Unit = { }
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
                AndroidView(
                    modifier = Modifier.size(60.dp),
                    factory = { ImageView(it) }
                ) {
                    it.setImageResource(R.drawable.ic_logo_ddc)
                }

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

            Button(onClick = { clearDatabase() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_forever),
                    contentDescription = null
                )
                Text(text = stringResource(id = R.string.clean_cache).uppercase())
            }

            Spacer(modifier = Modifier.size(16.dp))

            Text(text = "v ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
        }
    }
}