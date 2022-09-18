package com.example.lifetrack.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifetrack.R
import com.example.lifetrack.presentation.ui.theme.Green
import com.example.lifetrack.presentation.ui.theme.Pink
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarDialog(
    modifier: Modifier = Modifier,
    onSubmitDateClicked: (LocalDate) -> Unit,
    onCancelClicked: () -> Unit
) {
    val calendarState = rememberSelectableCalendarState()
    val selectionState = calendarState.selectionState

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(modifier = modifier) {
        SelectableCalendar(
            horizontalSwipeEnabled = false,
            calendarState = calendarState,
            dayContent = { dayState ->
                if (dayState.date.month == calendarState.monthState.currentMonth.month) {
                    CalendarDate(
                        day = dayState.date.dayOfMonth.toString(),
                        isSelected = selectionState.isDateSelected(dayState.date),
                        onDateSelected = {
                            onDateSelected(date = dayState.date, receiver = selectionState)
                            selectedDate = dayState.date
                        }
                    )
                }
            },
            weekHeader = { daysList ->
                CalendarWeek(weekDays = daysList)
            },
            monthHeader = { monthState ->
                CalendarMonth(monthState = monthState)
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancelClicked) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
            TextButton(onClick = { onSubmitDateClicked(selectedDate) }) {
                Text(
                    text = stringResource(R.string.done),
                    color = Pink,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }
    }
}

@Composable
fun CalendarDate(day: String, isSelected: Boolean, onDateSelected: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Green else Color.Transparent,
        animationSpec = tween(durationMillis = 500)
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Black,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .aspectRatio(ratio = 1f)
            .padding(5.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(5.dp)
            )
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onDateSelected
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CalendarWeek(weekDays: List<DayOfWeek>) {
    Row {
        weekDays.forEach { day ->
            Text(
                text = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CalendarMonth(monthState: MonthState) {
    val monthsList by remember {
        mutableStateOf(mutableListOf<YearMonth>().apply {
            for (i in 0 until 5) {
                add(YearMonth.now().plusMonths(i.toLong()))
            }
        }.toList())
    }

    Box(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .fillMaxWidth()
            .background(Pink)
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (month in monthsList) {
                Text(
                    text = month.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable { monthState.currentMonth = month },
                    color = Color.White,
                    fontWeight = if (monthState.currentMonth == month) FontWeight.Bold else null,
                    fontSize = 17.sp
                )
            }
        }
        Text(
            text = "${monthState.currentMonth.year}",
            modifier = Modifier.align(Alignment.CenterEnd),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
    }
}

private fun onDateSelected(receiver: DynamicSelectionState, date: LocalDate) =
    receiver.onDateSelected(date)