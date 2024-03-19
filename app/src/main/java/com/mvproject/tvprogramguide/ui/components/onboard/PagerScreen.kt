package com.mvproject.tvprogramguide.ui.components.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mvproject.tvprogramguide.ui.screens.onboard.state.OnBoardingPage
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size12),
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth(MaterialTheme.dimens.fraction80)
                    .fillMaxHeight(MaterialTheme.dimens.fraction80),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image",
        )
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            text = stringResource(id = onBoardingPage.title),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = MaterialTheme.dimens.size40),
            text = stringResource(id = onBoardingPage.description),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
@PreviewLightDark
fun FirstOnBoardingScreenPreview() {
    TvGuideTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            PagerScreen(onBoardingPage = OnBoardingPage.First)
        }
    }
}

@Composable
@PreviewLightDark
fun SecondOnBoardingScreenPreview() {
    TvGuideTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            PagerScreen(onBoardingPage = OnBoardingPage.Second)
        }
    }
}

@Composable
@PreviewLightDark
fun ThirdOnBoardingScreenPreview() {
    TvGuideTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            PagerScreen(onBoardingPage = OnBoardingPage.Third)
        }
    }
}
