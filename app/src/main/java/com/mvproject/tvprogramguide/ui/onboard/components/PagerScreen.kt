package com.mvproject.tvprogramguide.ui.onboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tvprogramguide.theme.appTypography
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
            modifier = Modifier.padding(vertical = MaterialTheme.dimens.size24)
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
            color = MaterialTheme.colors.onPrimary,
            fontSize = MaterialTheme.dimens.font20,
            textAlign = TextAlign.Center,
            style = MaterialTheme.appTypography.textSemiBold
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.size40)
                .padding(top = MaterialTheme.dimens.size20),
            color = MaterialTheme.colors.onPrimary
                .copy(alpha = MaterialTheme.dimens.alpha70),
            text = stringResource(id = onBoardingPage.description),
            fontSize = MaterialTheme.dimens.font14,
            textAlign = TextAlign.Center,
            style = MaterialTheme.appTypography.textMedium
        )
    }
}

@Composable
@Preview(showBackground = true)
fun FirstOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.First)
    }
}

@Composable
@Preview(showBackground = true)
fun SecondOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.Second)
    }
}

@Composable
@Preview(showBackground = true)
fun ThirdOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.Third)
    }
}