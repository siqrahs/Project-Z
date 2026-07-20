package com.open.securitymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.open.securitymanager.core.theme.SecurityManagerTheme
import com.open.securitymanager.features.dashboard.presentation.DashboardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContent {
            SecurityManagerTheme {
                DashboardScreen()
            }
        }
    }
}
