package com.mvproject.tvprogramguide.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.mvproject.tvprogramguide.theme.dimens
import com.mvproject.tvprogramguide.ui.components.radio.RadioGroupScrollable

@Composable
fun ShowSelectFromListDialog(
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
                shadowElevation = MaterialTheme.dimens.size8
            ) {
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.size6),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(id = R.string.dlg_title_select_custom_user_list),
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.dimens.size4),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
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

                    ElevatedButton(
                        onClick = {
                            onSelected(name.value)
                            isDialogOpen.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(MaterialTheme.dimens.fraction50)
                            .padding(MaterialTheme.dimens.size4),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = stringResource(id = R.string.dlg_btn_close),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
