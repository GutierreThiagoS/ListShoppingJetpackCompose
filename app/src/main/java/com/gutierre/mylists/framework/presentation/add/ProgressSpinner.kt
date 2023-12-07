package com.gutierre.mylists.framework.presentation.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.framework.ui.theme.BackgroundSpinner

@Composable
fun ProgressSpinner() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(BackgroundSpinner),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = Color.Cyan,
            strokeWidth = 5.dp
        )
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProgressSpinnerPreview() {
    ProgressSpinner()
}