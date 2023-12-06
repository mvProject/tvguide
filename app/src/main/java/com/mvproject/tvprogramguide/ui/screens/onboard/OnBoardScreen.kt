package com.mvproject.tvprogramguide.ui.screens.onboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.ui.components.onboard.AnimatedCompleteButton
import com.mvproject.tvprogramguide.ui.components.onboard.PagerIndicator
import com.mvproject.tvprogramguide.ui.components.onboard.PagerScreen
import com.mvproject.tvprogramguide.ui.screens.onboard.state.OnBoardingPage
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tvprogramguide.utils.AppConstants.ONBOARD_PAGES_COUNT

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardScreen(
    viewModel: OnBoardViewModel,
    onComplete: () -> Unit
) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third
    )
    val pagerState = rememberPagerState(
        initialPage = COUNT_ZERO,
        initialPageOffsetFraction = COUNT_ZERO_FLOAT
    ) {
        pages.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                Orientation.Horizontal
            ),
            pageContent = { position ->
                PagerScreen(onBoardingPage = pages[position])
            }
        )

        PagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(MaterialTheme.dimens.weight1),
            size = ONBOARD_PAGES_COUNT,
            currentPage = pagerState.currentPage
        )

        AnimatedCompleteButton(
            modifier = Modifier.weight(MaterialTheme.dimens.weight1),
            currentPage = pagerState.currentPage
        ) {
            viewModel.completeOnBoard(completed = true)
            onComplete()
        }
    }
}
