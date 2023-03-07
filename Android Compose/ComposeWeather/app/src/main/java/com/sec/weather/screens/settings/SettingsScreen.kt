package com.sec.weather.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sec.weather.components.WeatherAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    val items = listOf("苹果", "香蕉", "樱桃", "草莓")
    Scaffold(topBar = {
        WeatherAppBar(
            title = "设置",
            icon = Icons.Default.ArrowBack,
            isMainScreen = false,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            val pagerState = rememberPagerState(initialPage = 0)
            val tabStr = remember { mutableStateOf(items[0]) }
            Column {
                val scope = rememberCoroutineScope()
                TabRow(
                    selectedTabIndex = items.indexOf(tabStr.value),
                    modifier = Modifier.fillMaxWidth(),
                    indicator = { tabIndicator ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabIndicator[items.indexOf(tabStr.value)])
                        )
                    },
                    divider = {}) {
                    items.forEachIndexed { index, s ->
                        val selected = index == items.indexOf(tabStr.value)
                        Tab(
                            selected = selected,
                            onClick = {
                                tabStr.value = items[index]
                                scope.launch {
                                    pagerState.scrollToPage(index)
                                }
                            },
                            text = {
                                Text(text = items[index])
                            }
                        )
                    }
                    tabStr.value = items[pagerState.currentPage]
                }
                HorizontalPager(count = items.size, state = pagerState) { pager ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = items[pager])
                        }
                    }
                }
            }
        }
    }
}