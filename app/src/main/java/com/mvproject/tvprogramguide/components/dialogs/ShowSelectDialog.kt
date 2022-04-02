package com.mvproject.tvprogramguide.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mvproject.tvprogramguide.components.radio.RadioGroupScrollable

@Composable
fun ShowSelectDialog(
    radioOptions: List<String> = listOf(),
    defaultSelection: Int = 0,
    isDialogOpen: MutableState<Boolean>,
    onSelected: (String) -> Unit
) {
    val name = remember { mutableStateOf("") }

    if (isDialogOpen.value) {
        Dialog(
            onDismissRequest = { isDialogOpen.value = false }
        ) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(5.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colors.primary,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    RadioGroupScrollable(
                        radioOptions = radioOptions,
                        defaultSelection = defaultSelection,
                    ) { selected ->
                        name.value = selected
                    }

                    Spacer(modifier = Modifier.padding(10.dp))

                    Button(
                        onClick = {
                            onSelected(name.value)
                            isDialogOpen.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(60.dp)
                            .padding(10.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onPrimary,
                        )
                    ) {
                        Text(
                            text = "Close",
                            color = MaterialTheme.colors.primary,
                            fontSize = 12.sp
                        )
                    }
                }

            }
        }
    }
}




