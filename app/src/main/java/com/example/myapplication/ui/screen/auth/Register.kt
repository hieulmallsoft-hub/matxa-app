package com.example.myapplication.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun CreateAccountScreen(
    onCloseClick: () -> Unit = {},
    onContinueClick: (email: String, sessionId: String) -> Unit = { _, _ -> },
    onLoginClick: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFFFFBF5))
    ) {

        // Toàn bộ nội dung chính
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // =========================
            // Banner phía trên
            // =========================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(285.dp)

            ) {

                Image(
                    painter = painterResource(R.drawable.regis),
                    contentDescription = "Register banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(
                            start = 12.dp,
                            top = 16.dp
                        )
                ) {

                    Icon(
                        painter = painterResource(R.drawable.close),
                        contentDescription = "Đóng",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            // =========================
            // Phần form
            // =========================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            ) {

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Image(
                    painter = painterResource(R.drawable.logo_psycore),
                    contentDescription = "Psycore",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(50.dp)
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = "Tạo tài khoản",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                OutlinedTextField(
                    value = uiState.value.email,
                    onValueChange = {
                        viewModel.updateEmail(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = "Nhập Gmail của bạn",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF7F4EC),
                        unfocusedContainerColor = Color(0xFFF7F4EC),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                Button(
                    onClick = {
                        viewModel.sendOtp { sessionId ->
                            onContinueClick(uiState.value.email.trim(), sessionId)
                        }
                    },
                    enabled = uiState.value.email.isNotBlank() && !uiState.value.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF222222),
                        disabledContainerColor = Color(0xFFB8C0C4)
                    )
                ) {

                    Text(
                        text = "Tiếp tục",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }

        // =========================
        // Đăng nhập nằm cuối màn hình
        // =========================
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,

        ) {

            Text(
                text = "Bạn đã có tài khoản? ",
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )

            Text(
                text = "Đăng nhập",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222),
                modifier = Modifier.clickable {
                    onLoginClick()
                }
            )
        }
    }
}
