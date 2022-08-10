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
import androidx.compose.ui.unit.dp
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
                    .height(250.dp)
                    .padding(MaterialTheme.dimens.size6),
                shape = RoundedCornerShape(MaterialTheme.dimens.size16),
                color = MaterialTheme.colors.primary,
                elevation = MaterialTheme.dimens.size8
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size6),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(id = R.string.add_new_custom_list),
                        modifier = Modifier.padding(MaterialTheme.dimens.size4),
                        style = MaterialTheme.appTypography.textSemiBold,
                        fontSize = MaterialTheme.dimens.font18
                    )

                    Spacer(modifier = Modifier.padding(MaterialTheme.dimens.size4))

                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        textStyle = MaterialTheme.appTypography.textMedium,
                        placeholder = {
                            Text(text = stringResource(id = R.string.new_custom_hint))
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedLabelColor = MaterialTheme.colors.onBackground,
                            focusedLabelColor = MaterialTheme.colors.onSecondary,
                            textColor = MaterialTheme.colors.onPrimary
                        )
                    )

                    Spacer(modifier = Modifier.padding(MaterialTheme.dimens.size8))

                    Button(
                        onClick = {
                            onSelected(name.value)
                            isDialogOpen.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(MaterialTheme.dimens.size4),
                        shape = RoundedCornerShape(MaterialTheme.dimens.size8),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.btn_close),
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.appTypography.textSemiBold,
                            fontSize = MaterialTheme.dimens.font12
                        )
                    }
                }
            }
        }
    }
}
