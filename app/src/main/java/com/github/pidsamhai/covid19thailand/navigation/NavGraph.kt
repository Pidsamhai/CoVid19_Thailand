package com.github.pidsamhai.covid19thailand.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.timeline.TimelinePage
import com.github.pidsamhai.covid19thailand.ui.today.TodayPage
import com.github.pidsamhai.covid19thailand.ui.worldwide.WorldWidePage

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    subtitleCallback: SubtitleCallback
) {

    NavHost(
        modifier = Modifier.padding(paddingValues = paddingValues),
        navController = navController,
        startDestination = NavRoute.Today.route
    ) {
        composable(NavRoute.Today.route) {
            TodayPage(
                subtitleCallback = subtitleCallback
            )
        }

        composable(NavRoute.TimeLine.route) {
            TimelinePage(
                subtitleCallback = subtitleCallback
            )
        }

        composable(NavRoute.WorldWide.route) {
            WorldWidePage(
                subtitleCallback = subtitleCallback
            )
        }

        composable(NavRoute.About.route) {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = "About"
            )
        }
    }
}