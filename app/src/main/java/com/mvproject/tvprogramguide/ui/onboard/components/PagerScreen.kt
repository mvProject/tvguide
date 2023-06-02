package com.mvproject.tvprogramguide.ui.onboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.onboard.OnBoardingPage

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(
            modifier = Modifier.height(MaterialTheme.dimens.size24)
        )
        Image(
            modifier = Modifier
                .fillMaxWidth(MaterialTheme.dimens.fraction80)
                .fillMaxHeight(MaterialTheme.dimens.fraction70)
                .padding(top = MaterialTheme.dimens.size16),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = onBoardingPage.title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.size40)
                .padding(top = MaterialTheme.dimens.size20),
            text = stringResource(id = onBoardingPage.description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview(showBackground = true)
fun FirstOnBoardingScreenPreview() {
    TvGuideTheme() {
        Column(modifier = Modifier.fillMaxSize()) {
            PagerScreen(onBoardingPage = OnBoardingPage.First)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SecondOnBoardingScreenPreview() {
    TvGuideTheme() {
        Column(modifier = Modifier.fillMaxSize()) {
            PagerScreen(onBoardingPage = OnBoardingPage.Second)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ThirdOnBoardingScreenPreview() {
    TvGuideTheme() {
        Column(modifier = Modifier.fillMaxSize()) {
            PagerScreen(onBoardingPage = OnBoardingPage.Third)
        }
    }
}
