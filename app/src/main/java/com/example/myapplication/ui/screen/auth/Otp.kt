package com.example.myapplication.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    email: String,
    registrationSessionId: String,
    onCloseClick: () -> Unit = {},
    onOtpComplete: (String) -> Unit = {},
    viewModel: OtpViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(email, registrationSessionId) {
        viewModel.initialize(email, registrationSessionId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF5))
    ) {

        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 8.dp, top = 8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.close),
                contentDescription = "Đóng",
                tint = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 120.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Nhập mã OTP",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Mã xác nhận mã OTP đã gửi đến gmail",
                fontSize = 13.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )

            Text(
                text = email,
                fontSize = 13.sp,
                color = Color(0xFF222222),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(36.dp))

            OtpInput(
                otp = uiState.value.otp,
                onOtpChange = { value ->
                    viewModel.updateOtp(value)
                    if (value.length == 6 && value.all { it.isLetterOrDigit() }) {
                        viewModel.verifyOtp(onOtpComplete)
                    }
                }
            )

            Spacer(modifier = Modifier.height(38.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⌛",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Gửi lại mã sau ",
                    fontSize = 12.sp,
                    color = Color(0xFF555555)
                )

                Text(
                    text = String.format("00:%02d", uiState.value.secondsRemaining),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row {

                Text(
                    text = "Không nhận được mã? ",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )

                Text(
                    text = "Gửi lại mã",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (uiState.value.secondsRemaining == 0)
                        Color(0xFF222222)
                    else
                        Color.Gray,
                    modifier = Modifier.clickable(
                        enabled = uiState.value.secondsRemaining == 0
                    ) {
                        viewModel.resend()
                    }
                )
            }
        }
    }
}

@Composable
fun OtpInput(
    otp: String,
    onOtpChange: (String) -> Unit
) {
    BasicTextField(
        value = otp,
        onValueChange = onOtpChange,
        textStyle = TextStyle(
            color = Color.Transparent
        ),
        decorationBox = {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                repeat(6) { index ->

                    val character =
                        if (index < otp.length) {
                            otp[index].toString()
                        } else {
                            ""
                        }

                    Box(
                        modifier = Modifier
                            .size(
                                width = 36.dp,
                                height = 42.dp
                            )
                            .background(
                                color = Color(0xFFF4F1E9),
                                shape = RoundedCornerShape(7.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = character,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF222222)
                        )
                    }

                    if (index < 5) {
                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )
                    }
                }
            }
        }
    )
}
