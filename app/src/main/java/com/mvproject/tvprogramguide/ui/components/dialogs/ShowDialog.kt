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
import com.mvproject.tvprogramguide.data.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.theme.appTypography
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun ShowDialog(
    isDialogOpen: MutableState<Boolean>,
    onSelected: (String) -> Unit
) {
    val name = remember { mutableStateOf(NO_VALUE_STRING) }

    if (isDialogOpen.value) {
        Dialog(
            onDismissRequest = { isDialogOpen.value = false }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.size250)
                    .padding(MaterialTheme.dimens.size6),
                shape = RoundedCornerShape(MaterialTheme.dimens.size16),
                elevation = MaterialTheme.dimens.size8
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.dimens.size6),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(id = R.string.dlg_title_add_new_custom_user_list),
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.size4),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.appTypography.textSemiBold,
                        fontSize = MaterialTheme.dimens.font18
                    )

                    Spacer(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.size4)
                    )

                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        modifier = Modifier
                            .fillMaxWidth(MaterialTheme.dimens.fraction80),
                        textStyle = MaterialTheme.appTypography.textMedium,
                        placeholder = {
                            Text(text = stringResource(id = R.string.dlg_hint_new_custom_user_list))
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = MaterialTheme.colors.onSurface,
                            unfocusedBorderColor = MaterialTheme.colors.onSurface
                                .copy(alpha = MaterialTheme.dimens.alpha70),
                            focusedBorderColor = MaterialTheme.colors.onSurface
                                .copy(alpha = MaterialTheme.dimens.alpha30)
                        )
                    )

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
                            text = stringResource(id = R.string.dlg_btn_close),
                            style = MaterialTheme.appTypography.textMedium
                        )
                    }
                }
            }
        }
    }
}
