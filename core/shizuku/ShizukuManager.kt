package com.open.securitymanager.core.shizuku

import android.content.pm.PackageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import rikka.shizuku.Shizuku

enum class ShizukuState {
    CONNECTED,          // Shizuku aktif & izin diberikan
    PERMISSION_DENIED,  // Shizuku aktif tapi belum diizinkan
    NOT_RUNNING         // Service Shizuku mati / tidak terpasang
}

class ShizukuManager {

    private val _shizukuState = MutableStateFlow(ShizukuState.NOT_RUNNING)
    val shizukuState: StateFlow<ShizukuState> = _shizukuState.asStateFlow()

    private val onRequestPermissionResultListener =
        Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                _shizukuState.value = ShizukuState.CONNECTED
            } else {
                _shizukuState.value = ShizukuState.PERMISSION_DENIED
            }
        }

    private val onBinderReceivedListener = Shizuku.OnBinderReceivedListener {
        checkPermissionAndState()
    }

    private val onBinderDeadListener = Shizuku.OnBinderDeadListener {
        _shizukuState.value = ShizukuState.NOT_RUNNING
    }

    fun registerListeners() {
        Shizuku.addRequestPermissionResultListener(onRequestPermissionResultListener)
        Shizuku.addBinderReceivedListener(onBinderReceivedListener)
        Shizuku.addBinderDeadListener(onBinderDeadListener)
        checkPermissionAndState()
    }

    fun unregisterListeners() {
        Shizuku.removeRequestPermissionResultListener(onRequestPermissionResultListener)
        Shizuku.removeBinderReceivedListener(onBinderReceivedListener)
        Shizuku.removeBinderDeadListener(onBinderDeadListener)
    }

    fun checkPermissionAndState() {
        if (!Shizuku.pingBinder()) {
            _shizukuState.value = ShizukuState.NOT_RUNNING
            return
        }

        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            _shizukuState.value = ShizukuState.CONNECTED
        } else {
            _shizukuState.value = ShizukuState.PERMISSION_DENIED
        }
    }

    fun requestPermission(requestCode: Int = 1001) {
        if (Shizuku.pingBinder()) {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(requestCode)
            }
        }
    }
}
