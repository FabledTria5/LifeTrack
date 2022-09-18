package com.example.lifetrack.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifetrack.R
import com.example.lifetrack.domain.model.TaskItem
import com.example.lifetrack.domain.model.TaskType
import com.example.lifetrack.presentation.ui.theme.Green

@ExperimentalMaterial3Api
@Composable
fun DayTasksCard(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    tasksList: List<TaskItem>,
    onClick: () -> Unit,
    onTaskChecked: (TaskItem) -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.animateContentSize(tween(durationMillis = 500)),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = .3f))
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            TasksCardHeader(
                cardName = tasksList.first().taskType.displayName,
                isExpanded = isExpanded,
                tasksList = tasksList
            )
            if (isExpanded)
                repeat(times = tasksList.size) {
                    TaskListItem(
                        taskItem = tasksList[it],
                        modifier = Modifier.padding(vertical = 10.dp),
                        onClick = onTaskChecked
                    )
                }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun WeekTasksCard(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    tasksList: List<TaskItem>,
    onClick: () -> Unit,
    onTaskChecked: (TaskItem) -> Unit
) {
    var isListOpened by remember(isExpanded) {
        mutableStateOf(value = false)
    }

    LaunchedEffect(key1 = isExpanded) {
        if (!isExpanded) isListOpened = false
    }

    Card(
        onClick = onClick,
        modifier = modifier.animateContentSize(tween(durationMillis = 500)),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = .3f))
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            TasksCardHeader(
                cardName = TaskType.WEEK.displayName,
                isExpanded = isExpanded,
                tasksList = tasksList
            )
            if (isExpanded) {
                TaskListItem(
                    taskItem = tasksList.first(),
                    modifier = Modifier.padding(vertical = 10.dp),
                    onClick = onTaskChecked
                )
                AnimatedVisibility(
                    visible = !isListOpened,
                    exit = fadeOut(animationSpec = tween(durationMillis = 100))
                ) {
                    TextButton(
                        onClick = { isListOpened = !isListOpened },
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.see_all),
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
                AnimatedVisibility(
                    visible = isListOpened,
                    enter = expandVertically(
                        animationSpec = tween(durationMillis = 500, delayMillis = 100)
                    ),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 500))
                ) {
                    Column {
                        for (i in 1..tasksList.lastIndex) {
                            TaskListItem(
                                taskItem = tasksList[i],
                                modifier = Modifier.padding(vertical = 10.dp),
                                onClick = onTaskChecked
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TasksCardHeader(cardName: String, isExpanded: Boolean, tasksList: List<TaskItem>) {
    val arrowIconAnimation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    val completedTasksNumber by remember(tasksList) {
        derivedStateOf { tasksList.count { it.isComplete } }
    }

    val unCompletedTasksNumber by remember(completedTasksNumber) {
        mutableStateOf(value = tasksList.size - completedTasksNumber)
    }

    val tasksProgress by remember(completedTasksNumber, unCompletedTasksNumber) {
        mutableStateOf(value = (completedTasksNumber.toFloat() / tasksList.size))
    }

    val tasksProgressAnimation by animateFloatAsState(
        targetValue = tasksProgress,
        animationSpec = tween(durationMillis = 1000)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier) {
            Text(
                text = cardName,
                modifier = Modifier.padding(end = 20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                text = "$completedTasksNumber/${tasksList.size}",
                color = Color.Gray,
                fontSize = 15.sp
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier
                .size(25.dp)
                .rotate(degrees = arrowIconAnimation),
            tint = Color.Gray
        )
    }
    LinearProgressIndicator(
        progress = tasksProgressAnimation,
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(5.dp),
        color = Green,
        trackColor = Color.LightGray
    )
}

@Composable
private fun TaskListItem(
    taskItem: TaskItem,
    modifier: Modifier = Modifier,
    onClick: (TaskItem) -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            CustomCheckboxes.TaskCheckbox(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(30.dp),
                isComplete = taskItem.isComplete,
                onClick = { onClick(taskItem) }
            )
            Text(
                text = taskItem.taskName,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                textDecoration = if (taskItem.isComplete) TextDecoration.LineThrough else null,
                overflow = TextOverflow.Ellipsis
            )
        }
        Canvas(
            modifier = Modifier
                .weight(.1f)
                .height(15.dp)
        ) {
            drawCircle(color = Color(taskItem.taskPriority.color))
        }
    }
}