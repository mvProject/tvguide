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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mvproject.tvprogramguide.R

@Composable
fun ShowDialog(
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
                    .height(250.dp)
                    .padding(5.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.LightGray,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.padding(5.dp))

                    Text(
                        text = stringResource(id = R.string.add_new_custom_list),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        placeholder = { Text(text = "Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )


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
                            backgroundColor = MaterialTheme.colors.primary
                        )
                    ) {
                        Text(
                            text = "Close",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}


