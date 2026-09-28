package com.example.myapplication.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun LoginScreen(
    onCloseClick: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onAppleClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFFFBF5))) {
        Image(
            painter = painterResource(R.drawable.login),
            contentDescription = "Login banner",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 12.dp, top = 8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.close),
                contentDescription = "Đóng",
                tint = Color(0xFF222222)
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color(0xFFFFFCF7))
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo_psycore),
                    contentDescription = "Psycore",
                    colorFilter = ColorFilter.tint(Color(0xFF222222)),
                    modifier = Modifier.size(34.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Psycore", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222))
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onCreateAccountClick,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF282828))
            ) { Text("Tạo tài khoản", color = Color.White, fontWeight = FontWeight.Medium) }
            Spacer(Modifier.height(8.dp))
            SocialButton(R.drawable.logo_google, "Tiếp tục với Google", onGoogleClick)
            Spacer(Modifier.height(8.dp))
            SocialButton(R.drawable.apple, "Tiếp tục với Apple", onAppleClick)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Bạn đã có tài khoản? ", fontSize = 12.sp, color = Color(0xFF6B6B6B))
                Text(
                    text = "Đăng nhập",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222),
                    modifier = Modifier.clickable(onClick = onLoginClick)
                )
            }
        }
    }
}

@Composable
private fun SocialButton(iconId: Int, label: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(38.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFFE1DDD5)),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFFFFCF7))
    ) {
        Image(
            painter = painterResource(iconId),
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.width(10.dp))
        Text(label, fontSize = 14.sp, color = Color(0xFF222222))
    }
}
