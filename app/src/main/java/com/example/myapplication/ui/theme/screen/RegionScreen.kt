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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CountryUi(val code: String, val name: String, val flagEmoji: String)

private val supportedCountries = listOf(
    CountryUi("VN", "Việt Nam", "🇻🇳"), CountryUi("TH", "Thái Lan", "🇹🇭"),
    CountryUi("SG", "Singapore", "🇸🇬"), CountryUi("MY", "Malaysia", "🇲🇾"),
    CountryUi("ID", "Indonesia", "🇮🇩"), CountryUi("PH", "Philippines", "🇵🇭")
)

@Composable
fun SelectCountryScreen(initialCountryCode: String? = null, onContinueClick: (CountryUi) -> Unit) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCountryCode by rememberSaveable(initialCountryCode) { mutableStateOf(initialCountryCode) }
    val filteredCountries = remember(searchQuery) {
        supportedCountries.filter {
            it.name.contains(searchQuery, ignoreCase = true) || it.code.contains(searchQuery, ignoreCase = true)
        }
    }
    val selectedCountry = supportedCountries.find { it.code == selectedCountryCode }

    Box(Modifier.fillMaxSize().background(Color(0xFFF5F3EF)).statusBarsPadding().navigationBarsPadding()) {
        Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(20.dp))
            Text("Chọn quốc gia", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222))
            Spacer(Modifier.height(8.dp))
            Text("Bạn muốn sử dụng dịch vụ ở quốc gia nào?", fontSize = 15.sp, color = Color(0xFF6F6F6F))
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(searchQuery, { searchQuery = it }, Modifier.fillMaxWidth(), label = { Text("Tìm quốc gia") }, singleLine = true)
            Spacer(Modifier.height(12.dp))
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredCountries, key = { it.code }) { country ->
                    CountryItem(country, selectedCountryCode == country.code) { selectedCountryCode = country.code }
                }
            }
        }
        Button(
            onClick = { selectedCountry?.let(onContinueClick) }, enabled = selectedCountry != null,
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp).height(54.dp),
            shape = RoundedCornerShape(30.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F1F23))
        ) { Text("Tiếp tục", fontSize = 18.sp, color = Color.White) }
    }
}

@Composable
private fun CountryItem(country: CountryUi, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().border(1.dp, Color(0xFFD6D6D6), RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(country.flagEmoji, fontSize = 24.sp)
        Spacer(Modifier.width(12.dp))
        Text(country.name, fontSize = 17.sp, color = Color(0xFF222222))
        Spacer(Modifier.weight(1f))
        RadioButton(selected, onClick, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF1F1F23), unselectedColor = Color(0xFF6F6F6F)))
    }
}
