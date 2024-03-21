package com.mvproject.tvprogramguide.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.manageLength

@Composable
fun ShowAddNewDialog(
    isDialogOpen: MutableState<Boolean>,
    onSelected: (String) -> Unit,
) {
    val name = remember { mutableStateOf(NO_VALUE_STRING) }

    if (isDialogOpen.value) {
        Dialog(
            onDismissRequest = {
                name.value = NO_VALUE_STRING
                isDialogOpen.value = false
            },
        ) {
            Surface(
                modifier =
                    Modifier
                        .wrapContentSize()
                        .padding(MaterialTheme.dimens.size8),
                shape = MaterialTheme.shapes.small,
                shadowElevation = MaterialTheme.dimens.size8,
            ) {
                Column(
                    modifier =
                        Modifier
                            .padding(
                                horizontal = MaterialTheme.dimens.size32,
                                vertical = MaterialTheme.dimens.size16,
                            ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
                ) {
                    Text(
                        text = stringResource(id = R.string.dlg_title_add_new_custom_user_list),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    TextField(
                        value = name.value,
                        onValueChange = { typed ->
                            name.value = typed.manageLength()
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth(MaterialTheme.dimens.fraction80),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        placeholder = {
                            Text(text = stringResource(id = R.string.dlg_hint_new_custom_user_list))
                        },
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                            ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small,
                        colors =
                            TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.onSurface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                            ),
                    )

                    ElevatedButton(
                        onClick = {
                            onSelected(name.value)
                            name.value = NO_VALUE_STRING
                            isDialogOpen.value = false
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth(MaterialTheme.dimens.fraction50)
                                .padding(MaterialTheme.dimens.size4),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(
                            text = stringResource(id = R.string.dlg_btn_close),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
    }
}
