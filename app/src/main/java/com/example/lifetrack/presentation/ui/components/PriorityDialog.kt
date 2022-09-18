package com.example.lifetrack.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifetrack.R
import com.example.lifetrack.domain.model.TaskPriority
import com.example.lifetrack.presentation.ui.theme.Pink

@Composable
fun PriorityDialog(
    modifier: Modifier = Modifier,
    selectedMode: TaskPriority,
    onModeSelected: (TaskPriority) -> Unit,
    onCancelClicked: () -> Unit
) {
    val taskPriorities =
        listOf(TaskPriority.LowPriority, TaskPriority.MediumPriority, TaskPriority.HighPriority)

    Column(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Pink)
        )
        Text(
            text = stringResource(R.string.task_priority),
            modifier = Modifier.padding(15.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        taskPriorities.forEach { priority ->
            Row(
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
                    .fillMaxWidth()
                    .clickable { onModeSelected(priority) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(15.dp)
                    ) {
                        drawCircle(color = Color(priority.color))
                    }
                    Text(text = priority.name, fontSize = 16.sp)
                }
                CustomCheckboxes.RepeatTaskCheckbox(
                    isChecked = priority == selectedMode,
                    modifier = Modifier.size(25.dp),
                    onClick = { onModeSelected(priority) }
                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = onCancelClicked,
                modifier = Modifier.align(Alignment.CenterEnd),
                contentPadding = PaddingValues(15.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }
    }
}