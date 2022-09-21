package com.example.lifetrack.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifetrack.domain.model.Resource
import com.example.lifetrack.domain.model.TaskItem
import com.example.lifetrack.domain.model.TaskPriority
import com.example.lifetrack.domain.use_cases.GetUpcomingTasks
import com.example.lifetrack.domain.use_cases.RemoveOutdatedTasks
import com.example.lifetrack.domain.use_cases.SaveTask
import com.example.lifetrack.domain.use_cases.UpdateTask
import com.example.lifetrack.presentation.utils.capitalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
@Stable
class MainViewModel @Inject constructor(
    private val saveTask: SaveTask,
    private val getUpcomingTasks: GetUpcomingTasks,
    private val updateTask: UpdateTask,
    removeOutdatedTasks: RemoveOutdatedTasks
) : ViewModel() {

    companion object {
        private const val TASK_RESULT_MESSAGE_TIMEOUT = 5000L
        private const val TODAY_DATE_FORMAT = "dd MMM, EEEE"
    }

    private val _todayDate = MutableStateFlow(value = "")
    val todayDate = _todayDate.asStateFlow()

    private val _tasksList = MutableStateFlow<List<TaskItem>>(listOf())
    val tasksList = _tasksList.asStateFlow()

    private val _taskAddState = MutableStateFlow<Resource>(Resource.Idle)
    val taskAddState = _taskAddState.asStateFlow()

    private val _isCalendarVisible = MutableStateFlow(value = false)
    val isCalendarVisible = _isCalendarVisible.asStateFlow()

    private val _isPriorityVisible = MutableStateFlow(value = false)
    val isPriorityVisible = _isPriorityVisible.asStateFlow()

    private val _taskName = MutableStateFlow(value = "")
    val taskName = _taskName.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _selectedTasksPriority = MutableStateFlow<TaskPriority>(TaskPriority.LowPriority)
    val selectedPriority = _selectedTasksPriority.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) { removeOutdatedTasks() }
        setDate()
        getTasks()
    }

    private fun setDate() {
        val todayString =
            LocalDate.now().format(DateTimeFormatter.ofPattern(TODAY_DATE_FORMAT, Locale.ENGLISH))

        _todayDate.update {
            todayString.split(" ")
                .joinToString(separator = " ") { it.capitalize() }
                .replace(oldValue = ".", newValue = "")
        }
    }

    private fun getTasks() = getUpcomingTasks()
        .onEach { result ->
            _tasksList.update { result }
        }
        .flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)

    private fun clearTaskFields() {
        _taskName.update { "" }
        _selectedDate.update { LocalDate.now() }
        _selectedTasksPriority.update { TaskPriority.LowPriority }
    }

    fun onTaskNameChanged(newName: String) {
        _taskName.value = newName
    }

    fun onDateSelected(newDate: LocalDate) {
        _selectedDate.update { newDate }
        _isCalendarVisible.update { false }
    }

    fun onSelectPriority(priority: TaskPriority) {
        _selectedTasksPriority.update { priority }
    }

    fun toggleCalendar() {
        _isCalendarVisible.update { !it }
    }

    fun togglePriority() {
        _isPriorityVisible.update { !it }
    }

    fun createTask() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = saveTask(taskName.value, selectedDate.value, selectedPriority.value)) {
                Resource.Success -> {
                    _taskAddState.update { result }
                    clearTaskFields()
                }
                is Resource.Failure -> _taskAddState.update { result }
                Resource.Idle -> Unit
            }
            delay(timeMillis = TASK_RESULT_MESSAGE_TIMEOUT)
            _taskAddState.update { Resource.Idle }
        }
    }

    fun checkTask(taskItem: TaskItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTask(taskItem = taskItem)
        }
    }

}