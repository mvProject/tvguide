package com.mvproject.tvprogramguide.ui.onboard.view

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
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tvprogramguide.data.utils.AppConstants.ONBOARD_PAGES_COUNT
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.onboard.OnBoardingPage
import com.mvproject.tvprogramguide.ui.onboard.components.AnimatedCompleteButton
import com.mvproject.tvprogramguide.ui.onboard.components.PagerIndicator
import com.mvproject.tvprogramguide.ui.onboard.components.PagerScreen
import com.mvproject.tvprogramguide.ui.onboard.viewmodel.OnBoardViewModel

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
