package com.example.lifetrack.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifetrack.R
import com.example.lifetrack.domain.model.TaskPriority
import com.example.lifetrack.presentation.ui.theme.Green
import com.example.lifetrack.presentation.ui.theme.Pink
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun BottomSheetContent(
    taskName: String,
    onTaskNameChanged: (String) -> Unit,
    selectedDate: LocalDate,
    onSelectDateClicked: () -> Unit,
    selectedTasksPriority: TaskPriority,
    onSelectRepeatModeClicked: () -> Unit,
    onCreateTaskClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    val formattedDate by remember(selectedDate) {
        derivedStateOf {
            selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))
        }
    }

    val isSavable by remember(taskName) {
        derivedStateOf { taskName.isNotEmpty() }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 20.dp)
                .width(100.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
        )
        OutlinedTextField(
            value = taskName,
            onValueChange = onTaskNameChanged,
            textStyle = TextStyle(fontWeight = FontWeight.Medium),
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Pink,
                disabledBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Black
            )
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.due_date),
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                OutlinedTextField(
                    value = formattedDate,
                    onValueChange = {},
                    enabled = false,
                    textStyle = TextStyle(fontWeight = FontWeight.Medium),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .clickable { onSelectDateClicked() },
                    trailingIcon = {
                        IconButton(onClick = onSelectDateClicked) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                            )
                        }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledTrailingIconColor = Color.Gray
                    )
                )
            }
            Spacer(modifier = Modifier.weight(.1f))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.priority),
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                OutlinedTextField(
                    value = selectedTasksPriority.name,
                    onValueChange = {},
                    enabled = false,
                    textStyle = TextStyle(fontWeight = FontWeight.Medium),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .clickable { onSelectRepeatModeClicked() },
                    trailingIcon = {
                        IconButton(onClick = onSelectRepeatModeClicked) {
                            Icon(
                                painterResource(id = R.drawable.ic_priority),
                                contentDescription = null,
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledTrailingIconColor = Color.Gray
                    )
                )
            }
        }
        Button(
            onClick = onCreateTaskClicked,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green
            ),
            enabled = isSavable
        ) {
            Text(
                text = stringResource(R.string.add_new_task),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }
        TextButton(onClick = onDismiss) {
            Text(
                text = stringResource(R.string.back_to_list),
                color = Color.Gray
            )
        }
    }
}