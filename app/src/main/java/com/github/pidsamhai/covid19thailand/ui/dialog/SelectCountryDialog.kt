package com.github.pidsamhai.covid19thailand.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun SelectCountryDialog(
    title: String,
    selected: (country: String) -> Unit,
    items: List<String>?,
    showDialog: Boolean = false,
    onDismiss:() -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            SelectCountryDialogContent(
                title = title,
                selected = selected,
                items = items,
                onDismiss = onDismiss
            )
        }
    }
}


@Composable
fun SelectCountryDialogContent(
    title: String,
    selected: (country: String) -> Unit,
    items: List<String>?,
    onDismiss: () -> Unit
) {
    Card(
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
                    text = title,
                    style = MaterialTheme.typography.h6,
                    fontSize = 20.sp
                )
            }
            LazyColumn(
                modifier = Modifier.requiredSizeIn(maxHeight = 300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items ?: listOf()) {
                    Box(
                        Modifier
                            .defaultMinSize(minWidth = 280.dp)
                            .clickable {
                                selected(it)
                                onDismiss()
                            },
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                            text = it,
                            style = MaterialTheme.typography.body1,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .defaultMinSize(280.dp)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "CLOSE")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectCountryDialogPreView() {
    SelectCountryDialogContent(
        title = "Title",
        selected = { },
        onDismiss = { },
        items = listOf(
            "Thailand",
            "China"
        )
    )
}