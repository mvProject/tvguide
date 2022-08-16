package com.mvproject.tvprogramguide.ui.components.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.theme.dimens

@Composable
fun ShowAlertDialog() {
    var isDialogState by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(
            modifier = Modifier
                .height(40.dp)
                .weight(1f),
            onClick = {
                isDialogState = true
            },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.LightGray
            )
        ) {
            Text(text = "Show")
        }

        ShowAlertDialog(
            dialogState = isDialogState,
            onDismissRequest = { isDialogState = !it },
            onApply = { name ->
                // log $name
            }
        )
    }
}

@Composable
fun ShowAlertDialog(
    dialogState: Boolean,
    onDismissRequest: (dialogState: Boolean) -> Unit,
    onApply: (name: String) -> Unit
) {
    val name = remember { mutableStateOf("") }

    if (dialogState) {
        AlertDialog(
            backgroundColor = Color.DarkGray,
            onDismissRequest = {
                onDismissRequest(dialogState)
            },
            title = null,
            text = null,
            buttons = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(MaterialTheme.dimens.size8)
                ) {

                    Text(
                        text = stringResource(id = R.string.dlg_title_add_new_custom_user_list),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )

                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        placeholder = { Text(text = "Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1f),
                            onClick = {
                                onApply(name.value)
                                onDismissRequest(dialogState)
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color.Red,
                                contentColor = Color.Yellow
                            )
                        ) {
                            Text(text = "Apply")
                        }

                        TextButton(
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1f),
                            onClick = {
                                onDismissRequest(dialogState)
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color.DarkGray,
                                contentColor = Color.LightGray
                            )
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            shape = RoundedCornerShape(16.dp)
        )
    }
}
