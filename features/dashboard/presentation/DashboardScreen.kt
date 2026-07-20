package com.open.securitymanager.features.dashboard.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.open.securitymanager.core.shizuku.ShizukuState
import com.open.securitymanager.core.theme.XiaomiGreen
import com.open.securitymanager.core.theme.XiaomiOrange
import com.open.securitymanager.core.theme.XiaomiRed

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = DashboardViewModel(),
    onFeatureClick: (String) -> Unit = {}
) {
    val shizukuState by viewModel.shizukuState.collectAsState()

    val statusColor by animateColorAsState(
        targetValue = when (shizukuState) {
            ShizukuState.CONNECTED -> XiaomiGreen
            ShizukuState.PERMISSION_DENIED -> XiaomiOrange
            ShizukuState.NOT_RUNNING -> XiaomiRed
        },
        animationSpec = tween(500), label = "StatusColorAnimation"
    )

    val statusText = when (shizukuState) {
        ShizukuState.CONNECTED -> "Shizuku Terhubung"
        ShizukuState.PERMISSION_DENIED -> "Izin Shizuku Ditolak"
        ShizukuState.NOT_RUNNING -> "Shizuku Tidak Terhubung"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // --- Top Bar / Title ---
        Text(
            text = "Keamanan Sistem",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- Main Health Score & Shizuku Status Banner ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(statusColor)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicator Status Pulse Circle
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = statusText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (shizukuState == ShizukuState.CONNECTED)
                        "Sistem siap dieksekusi tanpa Root"
                    else
                        "Buka Shizuku untuk mengaktifkan fitur tingkat lanjut",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (shizukuState != ShizukuState.CONNECTED) {
                    Button(
                        onClick = { viewModel.requestShizukuPermission() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Hubungkan Shizuku",
                            color = statusColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Menu Grid Fitur Utama (Xiaomi Security Grid Style) ---
        Text(
            text = "Fitur Utama",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                FeatureCard(
                    title = "Cleaner Apps",
                    subtitle = "Sisa Cache & File Corpse",
                    iconText = "🧹",
                    onClick = { onFeatureClick("cleaner") }
                )
            }
            item {
                FeatureCard(
                    title = "Scanner",
                    subtitle = "Signature & VirusTotal",
                    iconText = "🛡️",
                    onClick = { onFeatureClick("scanner") }
                )
            }
            item {
                FeatureCard(
                    title = "Manage Apps",
                    subtitle = "Freeze & Debloater",
                    iconText = "⚙️",
                    onClick = { onFeatureClick("manageapps") }
                )
            }
            item {
                FeatureCard(
                    title = "Game Space",
                    subtitle = "DND Mode & RAM Boost",
                    iconText = "🚀",
                    onClick = { onFeatureClick("gamespace") }
                )
            }
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    subtitle: String,
    iconText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = iconText, fontSize = 24.sp)
            }
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
