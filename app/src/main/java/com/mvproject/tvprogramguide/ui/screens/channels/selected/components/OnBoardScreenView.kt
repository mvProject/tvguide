package com.mvproject.tvprogramguide.ui.screens.channels.selected

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mvproject.tvprogramguide.ui.components.onboard.AnimatedCompleteButton
import com.mvproject.tvprogramguide.ui.components.onboard.PagerIndicator
import com.mvproject.tvprogramguide.ui.components.onboard.PagerScreen
import com.mvproject.tvprogramguide.ui.screens.channels.selected.components.OnBoardingPage
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants

@Composable
fun OnBoardScreenView(onComplete: (Boolean) -> Unit = {}) {
    val pages =
        listOf(
            OnBoardingPage.First,
            OnBoardingPage.Second,
            OnBoardingPage.Third,
        )
    val pagerState =
        rememberPagerState(
            initialPage = AppConstants.COUNT_ZERO,
            initialPageOffsetFraction = AppConstants.COUNT_ZERO_FLOAT,
        ) {
            pages.size
        }

    Column(
        modifier =
            Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size12),
    ) {
        HorizontalPager(
            modifier =
                Modifier.fillMaxWidth().weight(1f),
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

        PagerIndicator(
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentHeight(),
            size = AppConstants.ONBOARD_PAGES_COUNT,
            currentPage = pagerState.currentPage,
        )

        AnimatedCompleteButton(
            currentPage = pagerState.currentPage,
        ) {
            onComplete(true)
        }
    }
}

@Composable
@PreviewLightDark
private fun OnBoardScreenViewPreview() {
    TvGuideTheme {
        OnBoardScreenView()
    }
}
