package com.example.lifetrack.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lifetrack.R
import com.example.lifetrack.domain.model.Resource
import com.example.lifetrack.domain.model.TaskType
import com.example.lifetrack.presentation.MainViewModel
import com.example.lifetrack.presentation.ui.components.BottomSheetContent
import com.example.lifetrack.presentation.ui.components.CalendarDialog
import com.example.lifetrack.presentation.ui.components.DayTasksCard
import com.example.lifetrack.presentation.ui.components.PriorityDialog
import com.example.lifetrack.presentation.ui.components.WeekTasksCard
import com.example.lifetrack.presentation.ui.theme.Green
import com.example.lifetrack.presentation.ui.theme.Pink
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val taskAddState by mainViewModel.taskAddState.collectAsStateWithLifecycle()
    val todayDate by mainViewModel.todayDate.collectAsStateWithLifecycle()

    val isCalendarDialogVisible by mainViewModel.isCalendarVisible.collectAsStateWithLifecycle()
    val isPriorityDialogVisible by mainViewModel.isPriorityVisible.collectAsStateWithLifecycle()

    val taskName by mainViewModel.taskName.collectAsStateWithLifecycle()
    val selectedDate by mainViewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedRepeatMode by mainViewModel.selectedPriority.collectAsStateWithLifecycle()

    val weekTasks by mainViewModel.tasksList.collectAsStateWithLifecycle()

    val hasTasks by remember(weekTasks) {
        derivedStateOf { weekTasks.isNotEmpty() }
    }

    val todayTasks by remember(weekTasks) {
        derivedStateOf { weekTasks.filter { it.taskType == TaskType.TODAY } }
    }

    val tomorrowTasks by remember(weekTasks) {
        derivedStateOf { weekTasks.filter { it.taskType == TaskType.TOMORROW } }
    }

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = tween(durationMillis = 500),
        confirmStateChange = {
            focusManager.clearFocus()
            true
        }
    )

    val taskNameDone = remember(focusManager) {
        { focusManager.clearFocus() }
    }

    val createTask = remember(mainViewModel) {
        {
            scope.launch { bottomSheetState.hide() }
            mainViewModel.createTask()
        }
    }

    val bottomSheetDismiss = remember(bottomSheetState) {
        {
            scope.launch {
                bottomSheetState.hide()
            }
            Unit
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .imePadding()
                    .fillMaxWidth(),
                taskName = taskName,
                selectedDate = selectedDate,
                selectedTasksPriority = selectedRepeatMode,
                onSelectDateClicked = mainViewModel::toggleCalendar,
                onTaskNameChanged = mainViewModel::onTaskNameChanged,
                onTaskNameDone = taskNameDone,
                onSelectRepeatModeClicked = mainViewModel::togglePriority,
                onCreateTaskClicked = createTask,
                onDismiss = bottomSheetDismiss
            )
        },
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        scrimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
    ) {
        Scaffold(
            topBar = { TopBar(taskAddState = taskAddState) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            bottomSheetState.show()
                        }
                    },
                    containerColor = Pink
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.button_add),
                        tint = Color.White
                    )
                }
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        top = padding.calculateTopPadding(),
                        end = 10.dp,
                        bottom = padding.calculateBottomPadding()
                    )
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                DateHeader(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .height(55.dp),
                    todayDate = todayDate
                )
                if (hasTasks) {
                    var isWeekTasksExpanded by remember {
                        mutableStateOf(value = todayTasks.isEmpty() && tomorrowTasks.isEmpty())
                    }
                    AnimatedVisibility(
                        visible = todayTasks.isNotEmpty(),
                        enter = fadeIn(animationSpec = tween(durationMillis = 500))
                    ) {
                        var isExpanded by remember { mutableStateOf(value = true) }
                        DayTasksCard(
                            modifier = Modifier.padding(vertical = 10.dp),
                            isExpanded = isExpanded,
                            tasksList = todayTasks,
                            onClick = { isExpanded = !isExpanded },
                            onTaskChecked = mainViewModel::checkTask
                        )
                    }
                    AnimatedVisibility(
                        visible = tomorrowTasks.isNotEmpty(),
                        enter = fadeIn(animationSpec = tween(durationMillis = 500))
                    ) {
                        var isExpanded by remember { mutableStateOf(value = false) }
                        DayTasksCard(
                            modifier = Modifier.padding(vertical = 10.dp),
                            isExpanded = isExpanded,
                            tasksList = tomorrowTasks,
                            onClick = { isExpanded = !isExpanded },
                            onTaskChecked = mainViewModel::checkTask
                        )
                    }
                    WeekTasksCard(
                        modifier = Modifier.padding(vertical = 10.dp),
                        isExpanded = isWeekTasksExpanded,
                        tasksList = weekTasks,
                        onClick = { isWeekTasksExpanded = !isWeekTasksExpanded },
                        onTaskChecked = mainViewModel::checkTask
                    )
                }
            }
            if (isCalendarDialogVisible) {
                Dialog(
                    onDismissRequest = mainViewModel::toggleCalendar,
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    CalendarDialog(
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .animateContentSize(),
                        onSubmitDateClicked = mainViewModel::onDateSelected,
                        onCancelClicked = mainViewModel::toggleCalendar
                    )
                }
            }
            if (isPriorityDialogVisible) {
                Dialog(
                    onDismissRequest = mainViewModel::togglePriority,
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    PriorityDialog(
                        modifier = Modifier
                            .padding(30.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.background),
                        selectedMode = selectedRepeatMode,
                        onModeSelected = mainViewModel::onSelectPriority,
                        onCancelClicked = mainViewModel::togglePriority
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(taskAddState: Resource) {
    val appBarColor by animateColorAsState(
        targetValue = when (taskAddState) {
            is Resource.Failure -> Color.Red
            Resource.Success -> Green
            else -> Pink
        },
        animationSpec = tween(durationMillis = 500)
    )

    val topBarContentAlpha by animateFloatAsState(
        targetValue = if (taskAddState is Resource.Success) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(appBarColor)
            .statusBarsPadding()
            .padding(horizontal = 10.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.add_task_success),
            modifier = Modifier.alpha(topBarContentAlpha),
            color = Color.White,
            fontSize = 16.sp
        )
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .alpha(topBarContentAlpha),
            tint = Color.White
        )
    }
}

@Composable
fun DateHeader(modifier: Modifier = Modifier, todayDate: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = todayDate,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = stringResource(R.string.tasks_of_the_week),
            fontSize = 17.sp,
            color = Color.Black
        )
    }
}