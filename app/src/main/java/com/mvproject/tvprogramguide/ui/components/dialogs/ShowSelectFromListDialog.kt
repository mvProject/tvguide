package com.mvproject.tvprogramguide.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.ui.components.radio.RadioGroupContent
import com.mvproject.tvprogramguide.ui.theme.TvGuideTheme
import com.mvproject.tvprogramguide.ui.theme.dimens
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.AppConstants.empty

@Composable
fun ShowSelectFromListDialog(
    channelLists: List<ChannelList>,
    defaultSelection: Int = COUNT_ZERO,
    isDialogOpen: MutableState<Boolean>,
    onSelected: (ChannelList) -> Unit = {},
) {
    val selection = channelLists.firstOrNull { it.isSelected }?.listName ?: String.empty

    var name by remember { mutableStateOf(selection) }

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

                    val scrollState = rememberScrollState()

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(MaterialTheme.dimens.weight1)
                            .verticalScroll(
                                state = scrollState,
                                enabled = true,
                            ),
                    ) {
                        RadioGroupContent(
                            radioOptions = channelLists.map { it.listName },
                            defaultSelection = defaultSelection,
                            onItemClick = { selected ->
                                name = selected
                            },
                        )
                    }

                    ElevatedButton(
                        onClick = {
                            channelLists.firstOrNull { it.listName == name }?.let {
                                onSelected(it)
                            }
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
@Composable
fun ShowSelectFromListDialogPreview() {
    val open =
        remember {
            mutableStateOf(true)
        }
    TvGuideTheme {
        ShowSelectFromListDialog(
            channelLists = listOf(ChannelList(1, "1", true), ChannelList(2, "2", false)),
            defaultSelection = 1,
            isDialogOpen = open,
            onSelected = {},
        )
    }
}
