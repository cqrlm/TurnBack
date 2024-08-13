package com.example.turnback.ui.bars

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.turnback.navigaiton.Screen
import com.example.ui.theme.TurnBackTheme

@Composable
fun BottomNavBar(navController: NavHostController, changeScreen: (Screen) -> Unit) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomBar(
        selected = { screen ->
            currentDestination?.hierarchy?.any { it.route == screen.route } == true
        },
        onClick = { screen ->
            navController.navigate(screen.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
            changeScreen(screen)
        }
    )
}

@Composable
private fun BottomBar(selected: (Screen) -> Boolean, onClick: (Screen) -> Unit) {
    val items = remember {
        listOf(Screen.BottomBarItem.Timer, Screen.BottomBarItem.Stopwatch)
    }

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = selected(screen),
                onClick = { onClick(screen) },
                label = { Text(text = stringResource(id = screen.titleId)) },
                icon = {
                    Icon(
                        painter = painterResource(screen.iconId),
                        contentDescription = screen.route
                    )
                }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomBarPreview() {
    TurnBackTheme {
        BottomBar(selected = { screen -> screen is Screen.BottomBarItem.Timer }) {}
    }
}
