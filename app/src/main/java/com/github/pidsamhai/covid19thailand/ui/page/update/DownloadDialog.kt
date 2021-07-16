package com.github.pidsamhai.covid19thailand.ui.page.update

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.github.pidsamhai.covid19thailand.BuildConfig
import com.github.pidsamhai.covid19thailand.network.Download
import com.github.pidsamhai.covid19thailand.ui.viewmodel.DownloadDialogVM
import timber.log.Timber
import java.io.File

@Composable
fun DownloadDialogContent(
    onDismiss: () -> Unit,
    viewModel: DownloadDialogVM
) {

    val context = LocalContext.current

    val release = viewModel.releaseItem

    val download by viewModel.download.collectAsState(null)

    val fileSize = ((release.assets?.firstOrNull()?.size ?: 0) / 1024f) / 1024f

    LaunchedEffect(key1 = Unit) {
        viewModel.dlFile(file = File(context.filesDir, "app-release.apk"))
    }

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.cancel() }
    }

    val installApk: (file: File) -> Unit = {
        viewModel.removeReleaseItem()
        val uri: Uri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".provider",
            it
        )
        Timber.d(uri.toString())
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/vnd.android.package-archive")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            )
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.defaultMinSize(minWidth = 280.dp)
        ) {
            Column(
                modifier = Modifier
                    .height(64.dp)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (download !is Download.Finished) "Downloading" else "Download finished",
                    style = MaterialTheme.typography.h6,
                    fontSize = 20.sp
                )
                if (download !is Download.Finished) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CompositionLocalProvider(LocalContentAlpha provides 0.5f) {
                            if (download is Download.Progress) {
                                Text(
                                    text = ("%.2f".format(
                                        fileSize  * (download as Download.Progress).percent
                                    )) + "MB / ",
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = ("%.2f".format(fileSize)) + "MB",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            when (download) {
                is Download.Finished -> { Box { } }
                Download.Prepare -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
                is Download.Progress -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        progress = ((download as Download.Progress).percent)
                    )
                }
                else -> {
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (download is Download.Finished) {
                    TextButton(onClick = { installApk((download as Download.Finished).file) }) {
                        Text(text = "INSTALL")
                    }
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "CLOSE")
                    }
                }

                else {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "CANCEL")
                    }
                }
            }
        }
    }
}