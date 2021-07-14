package com.github.pidsamhai.covid19thailand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.pidsamhai.covid19thailand.navigation.NavGraph
import com.github.pidsamhai.covid19thailand.navigation.NavRoute
import com.github.pidsamhai.covid19thailand.ui.callback.SubtitleCallback
import com.github.pidsamhai.covid19thailand.ui.theme.AppTheme
import com.github.pidsamhai.covid19thailand.ui.widget.CustomAppBar
import com.github.pidsamhai.covid19thailand.utilities.toLastUpdate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                BodyContent()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BodyContent() {

    val navController = rememberNavController()

    val routes = listOf(
        NavRoute.Today,
        NavRoute.TimeLine,
        NavRoute.WorldWide,
        NavRoute.About,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val appBarTitle =
        (routes.find { it.route == currentDestination?.route } ?: NavRoute.Today).title
    var subtitle by remember { mutableStateOf<String?>(null) }

    val subtitleCallBack: SubtitleCallback = {
        subtitle = it
    }

    Scaffold(
        topBar = {
            CustomAppBar(
                title = stringResource(appBarTitle),
                subtitle = subtitle?.toLastUpdate()
            )
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color.White
            ) {
                routes.forEach {
                    BottomNavigationItem(
                        selected = currentDestination?.route == it.route,
                        icon = it.icon,
                        label = { Text(text = stringResource(it.label)) },
                        alwaysShowLabel = false,
                        onClick = {
                            navController.navigate(it.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavGraph(
            paddingValues = it,
            navController = navController,
            subtitleCallback = subtitleCallBack
        )
    }
}