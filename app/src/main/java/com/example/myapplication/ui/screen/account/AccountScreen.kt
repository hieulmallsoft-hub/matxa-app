package com.example.myapplication.ui.screen.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun AccountScreen(
    onHomeClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onCountryClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onPolicyClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color(0xFFFFFBF5),

        bottomBar = {
            AccountBottomNavigation(
                onHomeClick = onHomeClick
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(34.dp))

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF222222), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_psycore),
                    contentDescription = "Psyocore logo",
                    modifier = Modifier.size(36.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Chào mừng bạn đến với",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Psyocore",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF6F2E8)
                )
            ) {

                Column(
                    modifier = Modifier.padding(horizontal = 14.dp)
                ) {

                    AccountMenuItem(
                        icon = Icons.Outlined.HeadsetMic,
                        title = "Hỗ trợ",
                        onClick = onSupportClick
                    )

                    HorizontalDivider(
                        color = Color(0xFFE4DED3)
                    )

                    AccountMenuItem(
                        icon = Icons.Outlined.Translate,
                        title = "Ngôn ngữ",
                        value = "Tiếng Việt",
                        onClick = onLanguageClick
                    )

                    HorizontalDivider(
                        color = Color(0xFFE4DED3)
                    )

                    AccountMenuItem(
                        icon = Icons.Outlined.Language,
                        title = "Quốc gia",
                        value = "Việt Nam",
                        onClick = onCountryClick
                    )

                    HorizontalDivider(
                        color = Color(0xFFE4DED3)
                    )

                    AccountMenuItem(
                        icon = Icons.Outlined.Info,
                        title = "Về chúng tôi",
                        onClick = onAboutClick
                    )

                    HorizontalDivider(
                        color = Color(0xFFE4DED3)
                    )

                    AccountMenuItem(
                        icon = Icons.Outlined.Description,
                        title = "Điều khoản và chính sách",
                        onClick = onPolicyClick
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Đăng nhập để trải nghiệm dịch vụ tốt nhất",
                fontSize = 13.sp,
                color = Color(0xFF6E6E6E)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF222222)
                )
            ) {
                Text(
                    text = "Đăng nhập",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AccountMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(23.dp),
            tint = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            color = Color(0xFF333333),
            modifier = Modifier.weight(1f)
        )

        if (value != null) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.width(6.dp))
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF888888),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun AccountBottomNavigation(onHomeClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFFFFCF7))
                .padding(horizontal = 6.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AccountNavigationItem(false, Icons.Outlined.Home, "Khám phá", Modifier.weight(1f), onHomeClick)
            AccountNavigationItem(false, Icons.Outlined.ReceiptLong, "Hoạt động", Modifier.weight(1f)) {}
            AccountNavigationItem(false, Icons.Outlined.LocalOffer, "Ưu đãi", Modifier.weight(1f)) {}
            AccountNavigationItem(true, Icons.Outlined.Person, "Tài khoản", Modifier.weight(1f)) {}
        }
    }
}

@Composable
private fun AccountNavigationItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(if (selected) Color(0xFFF3F0E8) else Color.Transparent)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(22.dp),
            tint = Color(0xFF292929)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = Color(0xFF292929)
        )
    }
}

@Composable
fun LegacyAccountBottomNavigation(
    onHomeClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFFFFFBF5)
    ) {

        NavigationBarItem(
            selected = false,
            onClick = onHomeClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = null
                )
            },
            label = {
                Text("Khám phá", fontSize = 10.sp)
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ReceiptLong,
                    contentDescription = null
                )
            },
            label = {
                Text("Hoạt động", fontSize = 10.sp)
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(
                    imageVector = Icons.Outlined.LocalOffer,
                    contentDescription = null
                )
            },
            label = {
                Text("Ưu đãi", fontSize = 10.sp)
            }
        )

        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null
                )
            },
            label = {
                Text("Tài khoản", fontSize = 10.sp)
            }
        )
    }
}
