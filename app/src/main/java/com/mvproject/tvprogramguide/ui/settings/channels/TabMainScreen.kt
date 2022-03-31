package com.mvproject.tvprogramguide.ui.settings.channels

import androidx.compose.animation.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.theme.appColors
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.SELECTED_CHANNELS_PAGE
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun TabMainScreen(userListName: String) {
    val customListViewModel: CustomListViewModel = hiltViewModel()

    SideEffect {
        customListViewModel.setSelectedList(userListName)
    }


    val tabItems = listOf(
        stringResource(id = R.string.all_channels_title),
        stringResource(id = R.string.selected_channels_title)
    )
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.background(MaterialTheme.colors.primary)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(MaterialTheme.dimens.size4)
                .background(MaterialTheme.colors.primary)
                .clip(RoundedCornerShape(MaterialTheme.dimens.size30)),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .pagerTabIndicatorOffset(
                            pagerState, tabPositions
                        )
                        .background(MaterialTheme.colors.primary)
                        .width(MaterialTheme.dimens.sizeZero)
                        .height(MaterialTheme.dimens.sizeZero),
                    color = MaterialTheme.colors.primary
                )

            },
            divider = {
                TabRowDefaults.Divider(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary)
                        .width(MaterialTheme.dimens.sizeZero)
                        .height(MaterialTheme.dimens.sizeZero),
                )
            }
        ) {
            val startColor = MaterialTheme.appColors.backgroundPrimary
            val activeColor = MaterialTheme.appColors.backgroundPrimary
            val inActiveColor = MaterialTheme.appColors.backgroundSecondary

            tabItems.forEachIndexed { index, title ->
                val color = remember { Animatable(startColor) }
                LaunchedEffect(pagerState.currentPage == index) {
                    color.animateTo(
                        if (pagerState.currentPage == index)
                            activeColor else inActiveColor
                    )
                }

                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = if (pagerState.currentPage == index) {
                                TextStyle(
                                    color = MaterialTheme.colors.onPrimary,
                                    fontSize = MaterialTheme.dimens.font18
                                )
                            } else {
                                TextStyle(
                                    color = MaterialTheme.colors.onSecondary,
                                    fontSize = MaterialTheme.dimens.font16
                                )
                            }
                        )
                    },
                    modifier = Modifier.background(
                        color = color.value,
                        shape = RoundedCornerShape(MaterialTheme.dimens.size30)
                    )
                )
            }
        }

        HorizontalPager(
            count = tabItems.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
        ) { page ->
            when (page) {
                SELECTED_CHANNELS_PAGE -> {
                    SelectedChannelsScreen()
                }
                else -> {
                    AllChannelsScreen()
                }
            }
        }
    }
}