package com.hanzijourney.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hanzijourney.app.ui.lesson.LessonScreen
import com.hanzijourney.app.ui.screens.HomeScreen
import com.hanzijourney.app.ui.screens.LearnScreen
import com.hanzijourney.app.ui.screens.ProfileScreen
import com.hanzijourney.app.ui.screens.ReviewScreen
import com.hanzijourney.app.ui.screens.VocabularyScreen

@Composable
fun HanziAppRoot() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val showBottomBar = TopLevelDestination.entries.any { dest ->
        currentDestination?.hierarchy?.any { it.route == dest.route } == true
    }

    // Switching tabs keeps each tab's state; used by the bottom bar and by shortcuts such as "Ôn ngay".
    val goToTab: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) NavigationBar {
                TopLevelDestination.entries.forEach { dest ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true,
                        onClick = { goToTab(dest.route) },
                        icon = { Icon(dest.icon, contentDescription = null) },
                        label = { Text(stringResource(dest.labelRes)) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = TopLevelDestination.HOME.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(TopLevelDestination.HOME.route) { HomeScreen(onOpenReview = { goToTab(TopLevelDestination.REVIEW.route) }) }
            composable(TopLevelDestination.LEARN.route) {
                LearnScreen(onOpenLesson = { id -> navController.navigate("lesson/$id") })
            }
            composable(
                route = "lesson/{lessonId}",
                arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
            ) { entry ->
                LessonScreen(
                    lessonId = entry.arguments?.getString("lessonId").orEmpty(),
                    onClose = { navController.popBackStack() }
                )
            }
            composable(TopLevelDestination.VOCABULARY.route) { VocabularyScreen() }
            composable(TopLevelDestination.REVIEW.route) { ReviewScreen() }
            composable(TopLevelDestination.PROFILE.route) { ProfileScreen() }
        }
    }
}
