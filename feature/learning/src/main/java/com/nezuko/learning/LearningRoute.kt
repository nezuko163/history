package com.nezuko.learning

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun LearningRoute(
    onNavigateToCreateQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LearningScreen(
        modifier = modifier,
        onLearningMaterialsButtonClick = {
            Toast.makeText(
                context,
                "в разработке",
                Toast.LENGTH_SHORT
            ).show()
        },
        onInsertQuestionButtonClick = onNavigateToCreateQuestion
    )
}