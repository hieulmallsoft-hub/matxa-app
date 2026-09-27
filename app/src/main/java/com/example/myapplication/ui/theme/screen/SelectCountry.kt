package com.example.myapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CountryUi(
    val code: String,
    val name: String,
    val flagEmoji: String
)

@Composable
fun SelectCountryScreen(
    initialCountryCode: String? = null,
    onContinueClick: (CountryUi) -> Unit = {}
) {
    val countries = listOf(
        CountryUi("VN", "Vietnam", "\uD83C\uDDFB\uD83C\uDDF3")
    )

    var selectedCountry by remember(initialCountryCode) {
        mutableStateOf(countries.firstOrNull { it.code == initialCountryCode } ?: countries.first())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3EF))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Chọn quốc gia",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Bạn muốn sử dụng dịch vụ quốc gia nào",
                fontSize = 15.sp,
                color = Color(0xFF6F6F6F)
            )

            Spacer(modifier = Modifier.height(12.dp))

            countries.forEach { country ->
                CountryItem(
                    country = country,
                    selected = selectedCountry.code == country.code,
                    onClick = {
                        selectedCountry = country
                    }
                )
            }
        }

        Button(
            onClick = { onContinueClick(selectedCountry) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(54.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1F1F23)
            )
        ) {
            Text(
                text = "Tiếp tục",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun CountryItem(
    country: CountryUi,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFD6D6D6),
                shape = RoundedCornerShape(0.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = country.flagEmoji,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = country.name,
            fontSize = 18.sp,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.weight(1f))

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF222222),
                unselectedColor = Color(0xFF222222)
            )
        )
    }
}
