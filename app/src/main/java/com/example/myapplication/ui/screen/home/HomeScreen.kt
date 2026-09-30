package com.example.myapplication.ui.screen.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    onAccountClick: () -> Unit = {},
    onPhysicalHealthClick: () -> Unit = {},
    onMentalHealthClick: () -> Unit = {},
    onLocationClick: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var locationName by remember { mutableStateOf("Đang xác định vị trí...") }

    DisposableEffect(context) {
        if (!context.hasLocationPermission()) {
            locationName = "Chưa cấp quyền vị trí"
            onDispose { }
        } else {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val cancellationTokenSource = CancellationTokenSource()
            val listener = LocationListener { location ->
                scope.launch { locationName = context.cityNameFor(location) }
            }
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        scope.launch { locationName = context.cityNameFor(location) }
                    }
                }
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        scope.launch { locationName = context.cityNameFor(location) }
                    }
                }
                val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
                val lastLocation = providers.firstNotNullOfOrNull { provider ->
                    locationManager.getLastKnownLocation(provider)
                }
                if (lastLocation != null) {
                    scope.launch { locationName = context.cityNameFor(lastLocation) }
                }

                val enabledProviders = providers.filter(locationManager::isProviderEnabled)
                if (enabledProviders.isEmpty()) {
                    locationName = "Hãy bật dịch vụ vị trí"
                } else {
                    enabledProviders.forEach { provider ->
                        @Suppress("DEPRECATION")
                        locationManager.requestSingleUpdate(provider, listener, null)
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10_000L, 50f, listener)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10_000L, 50f, listener)
            } catch (_: SecurityException) {
                locationName = "Không thể đọc vị trí"
            } catch (_: IllegalArgumentException) {
                locationName = "Hãy bật dịch vụ vị trí"
            }
            onDispose {
                cancellationTokenSource.cancel()
                try {
                    locationManager.removeUpdates(listener)
                } catch (_: SecurityException) {
                    // Permission was revoked while this screen was open.
                }
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFFFFBF5),
        bottomBar = { HomeBottomNavigation(onAccountClick) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
        ) {
            HomeHeader(locationName)
            Spacer(Modifier.height(20.dp))
            HomeCard(R.drawable.component1_hd, "Sức khỏe thể chất", onPhysicalHealthClick)
            Spacer(Modifier.height(12.dp))
            HomeCard(R.drawable.component2_hd, "Sức khỏe tinh thần", onMentalHealthClick)
            Spacer(Modifier.height(12.dp))
            HomeCard(R.drawable.component3_hd, "Địa điểm", onLocationClick)
        }
    }
}

@Composable
private fun HomeHeader(locationName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF222222)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo_psycore),
                contentDescription = "Psycore",
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Vị trí của bạn", fontSize = 11.sp, color = Color(0xFF6F6F6F))
            Text(
                text = locationName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
        HeaderActionIcon(R.drawable.ic_notification_outline, "Thông báo")
        Spacer(Modifier.width(8.dp))
        HeaderActionIcon(R.drawable.ic_chat_outline, "Tin nhắn")
    }
}

@Composable
private fun HeaderActionIcon(resourceId: Int, description: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(resourceId),
            contentDescription = description,
            modifier = Modifier.size(22.dp),
            colorFilter = ColorFilter.tint(Color(0xFF222222))
        )
    }
}

@Composable
private fun LegacyHomeHeader(locationName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "✦",
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFF222222)),
            color = Color.White,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text("Vị trí của bạn⌄", fontSize = 11.sp, color = Color(0xFF222222))
            Text(locationName, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF222222))
        }
        Spacer(Modifier.weight(1f))
        Text("♧", fontSize = 24.sp, color = Color(0xFF222222))
        Spacer(Modifier.width(16.dp))
        Text("◌", fontSize = 24.sp, color = Color(0xFF222222))
    }
}

@Composable
private fun HomeCard(resourceId: Int, description: String, onClick: () -> Unit) {
    Image(
        painter = painterResource(resourceId),
        contentDescription = description,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun HomeBottomNavigation(onAccountClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .clip(RoundedCornerShape(52.dp))
                .background(Color(0xFFFFFCF7))
                .border(1.dp, Color(0xFFF1EDE5), RoundedCornerShape(52.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingNavigationItem(true, R.drawable.ic_nav_home, "Khám phá", Modifier.weight(1f)) {}
            FloatingNavigationItem(false, R.drawable.ic_nav_activity, "Hoạt động", Modifier.weight(1f)) {}
            FloatingNavigationItem(false, R.drawable.ic_nav_offer, "Ưu đãi", Modifier.weight(1f)) {}
            FloatingNavigationItem(false, R.drawable.ic_nav_account, "Tài khoản", Modifier.weight(1f), onAccountClick)
        }
    }
}

@Composable
private fun FloatingNavigationItem(
    selected: Boolean,
    iconId: Int,
    label: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(88.dp)
            .clip(RoundedCornerShape(44.dp))
            .background(if (selected) Color(0xFFF4F1E9) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(iconId),
            contentDescription = label,
            modifier = Modifier.size(28.dp),
            colorFilter = ColorFilter.tint(if (selected) Color(0xFF292929) else Color(0xFF3C3C3C))
        )
        Spacer(Modifier.height(7.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = Color(0xFF292929)
        )
    }
}

@Composable
private fun LegacyHomeBottomNavigation(onAccountClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFBF5))
            .padding(vertical = 8.dp)
    ) {
        HomeNavigationItem(true, "⌂", "Khám phá", Modifier.weight(1f)) {}
        HomeNavigationItem(false, "▣", "Hoạt động", Modifier.weight(1f)) {}
        HomeNavigationItem(false, "♧", "Ưu đãi", Modifier.weight(1f)) {}
        HomeNavigationItem(false, "♙", "Tài khoản", Modifier.weight(1f), onAccountClick)
    }
}

@Composable
private fun HomeNavigationItem(
    selected: Boolean,
    icon: String,
    label: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        val color = if (selected) Color(0xFF222222) else Color(0xFF777777)
        Text(icon, fontSize = 20.sp, color = color)
        Text(label, fontSize = 10.sp, color = color)
    }
}

private fun Context.hasLocationPermission(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

private suspend fun Context.cityNameFor(location: Location): String = withContext(Dispatchers.IO) {
    try {
        @Suppress("DEPRECATION")
        val address = Geocoder(this@cityNameFor, Locale.getDefault())
            .getFromLocation(location.latitude, location.longitude, 1)
            ?.firstOrNull()
        address?.locality ?: address?.subAdminArea ?: address?.adminArea ?: "Vị trí hiện tại"
    } catch (_: Exception) {
        "Vị trí hiện tại"
    }
}
