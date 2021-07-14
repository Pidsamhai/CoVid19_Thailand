package com.github.pidsamhai.covid19thailand.navigation

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.github.pidsamhai.covid19thailand.R

sealed class NavRoute(
    val route: String,
    @StringRes val title: Int,
    @StringRes val label: Int,
    val icon: @Composable () -> Unit
) {
    object Today: NavRoute(
        route = "today",
        label = R.string.today,
        title = R.string.title_today,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_today),
                contentDescription = LocalContext.current.resources.getString(R.string.today)
            )
        }
    )

    object TimeLine: NavRoute(
        route = "timeline",
        label = R.string.time_line,
        title = R.string.title_time_line,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_timeline),
                contentDescription = LocalContext.current.resources.getString(R.string.time_line)
            )
        }
    )

    object WorldWide: NavRoute(
        route = "worldwide",
        label = R.string.world,
        title = R.string.title_world,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_world_wide),
                contentDescription = LocalContext.current.resources.getString(R.string.world)
            )
        }
    )

    object About: NavRoute(
        route = "about",
        label = R.string.about,
        title = R.string.about,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_update),
                contentDescription = LocalContext.current.resources.getString(R.string.about)
            )
        }
    )

    object UpdateDialog: NavRoute(
        route = "update",
        label = 0,
        title = 0,
        icon = {  }
    )

    object DownloadDialog: NavRoute(
        route = "download",
        label = 0,
        title = 0,
        icon = {  }
    )
}