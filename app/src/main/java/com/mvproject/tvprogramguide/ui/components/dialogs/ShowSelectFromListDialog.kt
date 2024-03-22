package com.mvproject.tvprogramguide.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.window.Dialog
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.ui.components.radio.RadioGroupScrollable
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_STRING

@Composable
fun ShowSelectFromListDialog(
    radioOptions: List<String> = listOf(),
    defaultSelection: Int = COUNT_ZERO,
    isDialogOpen: MutableState<Boolean>,
    onSelected: (String) -> Unit,
) {
    val def =
        if (radioOptions.isEmpty()) {
            NO_VALUE_STRING
        } else {
            radioOptions[defaultSelection]
        }

    val name = remember { mutableStateOf(def) }
    if (isDialogOpen.value) {
        Dialog(
            onDismissRequest = { isDialogOpen.value = false },
        ) {
            Surface(
                modifier =
                    Modifier
                        .width(MaterialTheme.dimens.size350)
                        .height(MaterialTheme.dimens.size350)
                        .padding(MaterialTheme.dimens.size8),
                shape = MaterialTheme.shapes.small,
                shadowElevation = MaterialTheme.dimens.size8,
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = MaterialTheme.dimens.size16,
                                vertical = MaterialTheme.dimens.size12,
                            ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size12),
                ) {
                    Text(
                        text = stringResource(id = R.string.dlg_title_select_custom_user_list),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    RadioGroupScrollable(
                        radioOptions = radioOptions,
                        defaultSelection = defaultSelection,
                    ) { selected ->
                        name.value = selected
                    }

                    ElevatedButton(
                        onClick = {
                            onSelected(name.value)
                            isDialogOpen.value = false
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.dimens.size4),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
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

@PreviewLightDark
@PreviewScreenSizes
@Composable
fun ShowSelectFromListDialogPreview() {
    val open =
        remember {
            mutableStateOf(true)
        }
    TvGuideTheme {
        ShowSelectFromListDialog(
            radioOptions = listOf("One, Two, Three"),
            defaultSelection = 1,
            isDialogOpen = open,
            onSelected = {},
        )
    }
}
