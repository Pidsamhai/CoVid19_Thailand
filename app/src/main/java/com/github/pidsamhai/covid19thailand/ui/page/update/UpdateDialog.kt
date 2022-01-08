package com.github.pidsamhai.covid19thailand.ui.page.update

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.pidsamhai.covid19thailand.BuildConfig
import com.github.pidsamhai.covid19thailand.network.Download
import com.github.pidsamhai.covid19thailand.ui.viewmodel.UpdateDialogVM
import com.github.pidsamhai.covid19thailand.ui.widget.MarkwonWidget
import com.navelplace.jsemver.Version
import org.koin.androidx.compose.getViewModel

@Composable
fun UpdateDialog(
    showDialog: Boolean = false,
    onDismiss:() -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            UpdateDialogContent(onDismiss = onDismiss)
        }
    }
}

@Composable
fun UpdateDialogContent(
    onDismiss: () -> Unit,
    downLoad: () -> Unit = { },
    updateDialogVM: UpdateDialogVM = getViewModel()
) {

    val currentVersion = Version(BuildConfig.VERSION_NAME)

    val scrollState = rememberScrollState()

    SideEffect {
        updateDialogVM.getRelease()
    }

    val release by updateDialogVM.releaseItem.observeAsState()
    val download by updateDialogVM.download.collectAsState(null)

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
                    text = if (release == null) "Checking update" else "Update found",
                    style = MaterialTheme.typography.h6,
                    fontSize = 20.sp
                )
                if (release != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                    ) {
                        ProvideTextStyle(value = TextStyle.Default.copy(fontSize = 12.sp)) {
                            Text(text = "version: ${release?.tagName}")
                            Text(text = ("%.2f".format(((release?.assets?.firstOrNull()?.size ?: 0) / 1024f) / 1024f)) + "MB")
                        }
                    }
                }
            }

            if (release != null && download == null) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp)
                ) {
                    MarkwonWidget(
                        text = release!!.body!!
                    )
                }
            }
            if (release == null){
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Loading...",
                        fontSize = 12.sp
                    )
                }
            }

            when(download) {
                is Download.Finished -> { }
                Download.Prepare -> {
                    LinearProgressIndicator(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp))
                }
                is Download.Progress -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        progress = ((download as Download.Progress).percent)
                    )
                }
                else -> {}
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (release != null) {
                    if(currentVersion.olderThan(Version(release?.tagName!!))) {
                        TextButton(onClick = {
                            updateDialogVM.saveReleaseItem()
                            downLoad()
                        }) {
                            Text(text = "DOWNLOAD")
                        }
                    } else {
                        Toast.makeText(LocalContext.current, "You use latest version", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                }
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "CLOSE")
                }
            }
        }
    }
}