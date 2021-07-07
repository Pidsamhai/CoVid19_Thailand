package com.github.pidsamhai.covid19thailand.navigation

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.github.pidsamhai.covid19thailand.R

sealed class NavRoute(val route: String, @StringRes val title: Int, val icon: @Composable () -> Unit) {
    object Today: NavRoute(
        route = "today",
        title = R.string.today,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_today),
                contentDescription = LocalContext.current.resources.getString(R.string.today)
            )
        }
    )

    object TimeLine: NavRoute(
        route = "timeline",
        title = R.string.time_line,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_timeline),
                contentDescription = LocalContext.current.resources.getString(R.string.time_line)
            )
        }
    )

    object WorldWide: NavRoute(
        route = "worldwide",
        title = R.string.world,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_world_wide),
                contentDescription = LocalContext.current.resources.getString(R.string.world)
            )
        }
    )

    object About: NavRoute(
        route = "about",
        title = R.string.about,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_update),
                contentDescription = LocalContext.current.resources.getString(R.string.about)
            )
        }
    )
}