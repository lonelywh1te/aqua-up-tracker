package ru.lonelywh1te.aquaup.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import ru.lonelywh1te.aquaup.presentation.navigation.TabItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val tabItems = listOf(TabItem.Home, TabItem.History, TabItem.Settings)
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabItems.size }

    LaunchedEffect(selectedTabIndex) {
        if (!pagerState.isScrollInProgress) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabItems.forEachIndexed { index, tabItem ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(text = stringResource(tabItem.title))
                    },
                    icon = {
                        Icon(imageVector = ImageVector.vectorResource(tabItem.icon), contentDescription = null)
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            tabItems[index].screen()
        }
    }
}