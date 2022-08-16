package com.mvproject.tvprogramguide.ui.onboard.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mvproject.tvprogramguide.data.utils.AppConstants.ONBOARD_PAGES_COUNT
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.onboard.OnBoardingPage
import com.mvproject.tvprogramguide.ui.onboard.components.AnimatedCompleteButton
import com.mvproject.tvprogramguide.ui.onboard.components.PagerIndicator
import com.mvproject.tvprogramguide.ui.onboard.components.PagerScreen
import com.mvproject.tvprogramguide.ui.onboard.viewmodel.OnBoardViewModel

@OptIn(ExperimentalPagerApi::class)
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
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.weight(MaterialTheme.dimens.weight10),
            count = ONBOARD_PAGES_COUNT,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }

        PagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(MaterialTheme.dimens.weight1),
            size = ONBOARD_PAGES_COUNT,
            currentPage = pagerState.currentPage
        )

        AnimatedCompleteButton(
            modifier = Modifier.weight(MaterialTheme.dimens.weight1),
            pagerState = pagerState
        ) {
            viewModel.completeOnBoard(completed = true)
            onComplete()
        }
    }
}