package com.nezuko.learning

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.nezuko.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(
    onInsertQuestionButtonClick: () -> Unit,
    onLearningMaterialsButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Обучение")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(
                            context,
                            "не нажимай сюда...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(Spacing.default.extraLarge * 2)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onInsertQuestionButtonClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Создать вопрос")
            }

            Button(
                onClick = onLearningMaterialsButtonClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Перейти к материалам")
            }
        }
    }
}

@Preview
@Composable
private fun LearningScreenPreview() {
    LearningScreen(
        onInsertQuestionButtonClick = { /*TODO*/ },
        onLearningMaterialsButtonClick = { /*TODO*/ }
    )
}