package com.example.lifetrack.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lifetrack.presentation.ui.theme.Green

object CustomCheckboxes {

    @Composable
    fun RepeatTaskCheckbox(isChecked: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
        val borderColor by animateColorAsState(
            targetValue = if (isChecked) Green else Color.Gray,
            animationSpec = tween(durationMillis = 500)
        )
        val borderWidth by animateDpAsState(
            targetValue = if (isChecked) 6.dp else 1.dp,
            animationSpec = tween(durationMillis = 500)
        )

        Box(
            modifier = modifier
                .clip(CircleShape)
                .border(
                    border = BorderStroke(width = borderWidth, color = borderColor),
                    shape = CircleShape
                )
                .clickable { onClick() }
        )
    }

    @Composable
    fun TaskCheckbox(modifier: Modifier = Modifier, isComplete: Boolean, onClick: () -> Unit) {
        val iconSizeAnimation by animateDpAsState(
            targetValue = if (isComplete) 24.dp else 0.dp,
            animationSpec = tween(durationMillis = 500)
        )

        val iconAlphaAnimation by animateFloatAsState(
            targetValue = if (isComplete) 1f else 0f,
            animationSpec = tween(durationMillis = 500)
        )

        Box(
            modifier = modifier
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(7.dp))
                .clip(RoundedCornerShape(7.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(iconSizeAnimation)
                    .alpha(iconAlphaAnimation)
            )
        }
    }

}