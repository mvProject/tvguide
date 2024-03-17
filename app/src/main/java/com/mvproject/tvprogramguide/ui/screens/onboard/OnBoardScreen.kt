package com.mvproject.tvprogramguide.ui.screens.onboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    onComplete: () -> Unit,
) {
    val pages =
        listOf(
            OnBoardingPage.First,
            OnBoardingPage.Second,
            OnBoardingPage.Third,
        )
    val pagerState =
        rememberPagerState(
            initialPage = COUNT_ZERO,
            initialPageOffsetFraction = COUNT_ZERO_FLOAT,
        ) {
            pages.size
        }

    val scrollState = rememberScrollState()
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                .verticalScroll(scrollState),
    ) {
        HorizontalPager(
            modifier =
                Modifier.fillMaxWidth()
                    .fillMaxHeight(MaterialTheme.dimens.fraction60),
            state = pagerState,
            pageNestedScrollConnection =
                PagerDefaults.pageNestedScrollConnection(
                    state = pagerState,
                    orientation = Orientation.Horizontal,
                ),
            pageContent = { position ->
                PagerScreen(onBoardingPage = pages[position])
            },
        )

        Spacer(modifier = Modifier.weight(MaterialTheme.dimens.weight1))

        PagerIndicator(
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(MaterialTheme.dimens.size24),
            size = ONBOARD_PAGES_COUNT,
            currentPage = pagerState.currentPage,
        )

        AnimatedCompleteButton(
            modifier = Modifier.weight(MaterialTheme.dimens.weight1),
            currentPage = pagerState.currentPage,
        ) {
            viewModel.completeOnBoard(completed = true)
            onComplete()
        }
    }
}
