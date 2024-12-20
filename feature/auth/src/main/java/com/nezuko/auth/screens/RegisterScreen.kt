package com.nezuko.auth.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.nezuko.ui.theme.Spacing

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterButtonClick: (email: String, name: String, password: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            OutlinedTextField(
                value = email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                onValueChange = { email = it },
                label = { Text(text = "Почта") }
            )

            Spacer(modifier = Modifier.padding(Spacing.default.small))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text(text = "Имя пользователя") }
            )

            Spacer(modifier = Modifier.padding(Spacing.default.small))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = "Пароль") }
            )


            Spacer(modifier = Modifier.padding(Spacing.default.medium))

            FilledTonalButton(
                onClick = { onRegisterButtonClick(email, userName, password) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Войти")
            }
        }
    }
}