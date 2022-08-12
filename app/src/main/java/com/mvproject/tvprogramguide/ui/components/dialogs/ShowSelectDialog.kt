package com.mvproject.tvprogramguide.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.components.radio.RadioGroupScrollable

@Composable
fun ShowSelectDialog(
    radioOptions: List<String> = listOf(),
    defaultSelection: Int = COUNT_ZERO,
    isDialogOpen: MutableState<Boolean>,
    onSelected: (String) -> Unit
) {
    val def = if (radioOptions.isEmpty()) NO_VALUE_STRING
    else radioOptions[defaultSelection]

    val name = remember { mutableStateOf(def) }

    if (isDialogOpen.value) {
        Dialog(
            onDismissRequest = { isDialogOpen.value = false }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(MaterialTheme.dimens.size6),
                shape = RoundedCornerShape(MaterialTheme.dimens.size16),
                color = MaterialTheme.colors.primary,
                elevation = MaterialTheme.dimens.size8
            ) {
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.size6),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(id = R.string.dlg_title_select_list),
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.dimens.size4),
                        style = MaterialTheme.appTypography.textSemiBold,
                        fontSize = MaterialTheme.dimens.font18
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.size4)
                    )

                    RadioGroupScrollable(
                        radioOptions = radioOptions,
                        defaultSelection = defaultSelection,
                    ) { selected ->
                        name.value = selected
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.size8)
                    )

                    Button(
                        onClick = {
                            onSelected(name.value)
                            isDialogOpen.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(MaterialTheme.dimens.fraction50)
                            .padding(MaterialTheme.dimens.size4),
                        shape = RoundedCornerShape(MaterialTheme.dimens.size8),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSecondary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.btn_close),
                            style = MaterialTheme.appTypography.textSemiBold,
                        )
                    }
                }
            }
        }
    }
}
