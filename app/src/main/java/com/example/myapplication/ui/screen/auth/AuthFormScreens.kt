package com.example.myapplication.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EmailLoginScreen(
    onCloseClick: () -> Unit,
    onLoggedIn: () -> Unit,
    viewModel: EmailLoginViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    CredentialForm(
        title = "Đăng nhập",
        email = state.email,
        password = state.password,
        buttonText = "Đăng nhập",
        loading = state.isLoading,
        error = state.error,
        showEmail = true,
        onCloseClick = onCloseClick,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onSubmit = { viewModel.login(onLoggedIn) }
    )
}

@Composable
fun CreatePasswordScreen(
    registrationSessionId: String,
    onCloseClick: () -> Unit,
    onCompleted: () -> Unit,
    viewModel: CreatePasswordViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    CredentialForm(
        title = "Tạo mật khẩu",
        email = "",
        password = state.password,
        buttonText = "Hoàn tất",
        loading = state.isLoading,
        error = state.error,
        showEmail = false,
        onCloseClick = onCloseClick,
        onEmailChange = {},
        onPasswordChange = viewModel::updatePassword,
        onSubmit = { viewModel.completeRegistration(registrationSessionId, onCompleted) }
    )
}

@Composable
private fun CredentialForm(
    title: String,
    email: String,
    password: String,
    buttonText: String,
    loading: Boolean,
    error: String?,
    showEmail: Boolean,
    onCloseClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF5))
            .padding(horizontal = 24.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "×",
            fontSize = 28.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable(onClick = onCloseClick)
                .padding(bottom = 24.dp)
        )
        Text(text = title, fontSize = 28.sp, color = Color(0xFF222222))
        Spacer(Modifier.height(24.dp))
        if (showEmail) {
            AuthTextField(email, "Email", false, onEmailChange)
            Spacer(Modifier.height(12.dp))
        }
        AuthTextField(password, "Mật khẩu", true, onPasswordChange)
        error?.let {
            Text(text = it, color = Color(0xFFB3261E), fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onSubmit,
            enabled = !loading,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222))
        ) {
            if (loading) CircularProgressIndicator(Modifier.height(20.dp), color = Color.White, strokeWidth = 2.dp)
            else Text(buttonText)
        }
    }
}

@Composable
private fun AuthTextField(value: String, label: String, password: Boolean, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF7F4EC),
            unfocusedContainerColor = Color(0xFFF7F4EC)
        )
    )
}
