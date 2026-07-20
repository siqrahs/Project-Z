package com.open.securitymanager.features.dashboard.presentation

import androidx.lifecycle.ViewModel
import com.open.securitymanager.core.shizuku.ShizukuManager
import com.open.securitymanager.core.shizuku.ShizukuState
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel(
    private val shizukuManager: ShizukuManager = ShizukuManager()
) : ViewModel() {

    val shizukuState: StateFlow<ShizukuState> = shizukuManager.shizukuState

    init {
        shizukuManager.registerListeners()
    }

    fun requestShizukuPermission() {
        shizukuManager.requestPermission()
    }

    fun refreshStatus() {
        shizukuManager.checkPermissionAndState()
    }

    override fun onCleared() {
        super.onCleared()
        shizukuManager.unregisterListeners()
    }
}
