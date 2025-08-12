package ru.lonelywh1te.aquaup.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.presentation.history.HistoryScreen
import ru.lonelywh1te.aquaup.presentation.history.HistoryScreenState
import ru.lonelywh1te.aquaup.presentation.home.HomeScreen
import ru.lonelywh1te.aquaup.presentation.settings.SettingsScreen

sealed class TabItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val screen: @Composable () -> Unit,
) {
    data object Home: TabItem(
        title = R.string.home,
        icon = R.drawable.ic_home,
        screen = { HomeScreen() }
    )

    data object History: TabItem(
        title = R.string.history,
        icon = R.drawable.ic_history,
        screen = { HistoryScreen(state = HistoryScreenState.getPreviewState(), onEditHistoryDataClick = {}) }
    )

    data object Settings: TabItem(
        title = R.string.settings,
        icon = R.drawable.ic_settings,
        screen = { SettingsScreen() }
    )

}
