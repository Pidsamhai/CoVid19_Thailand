package com.github.pidsamhai.covid19thailand.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.github.pidsamhai.covid19thailand.ui.page.about.AboutPage
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.page.timeline.TimelinePage
import com.github.pidsamhai.covid19thailand.ui.page.today.TodayPage
import com.github.pidsamhai.covid19thailand.ui.page.update.DownloadDialogContent
import com.github.pidsamhai.covid19thailand.ui.page.update.UpdateDialogContent
import com.github.pidsamhai.covid19thailand.ui.page.worldwide.WorldWidePage
import org.koin.androidx.compose.getStateViewModel

@OptIn(ExperimentalComposeUiApi::class)
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
            subtitleCallback(null)
            AboutPage(
                openUpdateDialog = { navController.navigate(NavRoute.UpdateDialog.route) }
            )
        }

        dialog(
            NavRoute.UpdateDialog.route,
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            UpdateDialogContent(
                onDismiss = { navController.navigateUp() },
                downLoad = {
                    navController.navigate(NavRoute.DownloadDialog.route) {
                        popUpTo(NavRoute.About.route)
                    }
                }
            )
        }

        dialog(
            route = NavRoute.DownloadDialog.route,
            dialogProperties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            DownloadDialogContent(
                viewModel = getStateViewModel(),
                onDismiss = { navController.navigateUp() }
            )
        }
    }
}