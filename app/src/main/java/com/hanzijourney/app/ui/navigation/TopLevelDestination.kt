package com.hanzijourney.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.vector.ImageVector
import com.hanzijourney.app.R

enum class TopLevelDestination(val route: String, @StringRes val labelRes: Int, val icon: ImageVector) {
    HOME("home", R.string.tab_home, Icons.Filled.Home),
    LEARN("learn", R.string.tab_learn, Icons.Filled.LocationOn),
    VOCABULARY("vocabulary", R.string.tab_vocabulary, Icons.AutoMirrored.Filled.List),
    REVIEW("review", R.string.tab_review, Icons.Filled.Refresh),
    PROFILE("profile", R.string.tab_profile, Icons.Filled.Person)
}
