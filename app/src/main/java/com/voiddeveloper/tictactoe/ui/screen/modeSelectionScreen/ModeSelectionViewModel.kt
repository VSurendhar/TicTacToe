package com.voiddeveloper.tictactoe.ui.screen.modeSelectionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

data class ModeSelectionUiState(
    val showLocalDialog: Boolean = false,
    val showServerIpDialog: Boolean = false,
    val showConnectingDialog: Boolean = false,
    val showOnlineModeDialog: Boolean = false,
    val connectionError: String? = null,
    val serverIp: String = "",
    val serverPort: String = "8081"
)

class ModeSelectionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ModeSelectionUiState())
    val uiState: StateFlow<ModeSelectionUiState> = _uiState.asStateFlow()

    fun onLocalClick() {
        _uiState.update { it.copy(showLocalDialog = true) }
    }

    fun onRemoteClick() {
        _uiState.update { it.copy(showServerIpDialog = true) }
    }

    fun onDismissLocalDialog() {
        _uiState.update { it.copy(showLocalDialog = false) }
    }

    fun onDismissServerIpDialog() {
        _uiState.update { it.copy(showServerIpDialog = false) }
    }

    fun onDismissOnlineModeDialog() {
        _uiState.update { it.copy(showOnlineModeDialog = false) }
    }

    fun onConfirmServerIp(ip: String, port: String) {
        _uiState.update {
            it.copy(
                showServerIpDialog = false,
                showConnectingDialog = true,
                serverIp = ip,
                serverPort = port,
                connectionError = null
            )
        }
        viewModelScope.launch {
            val success = makePingPong(ip, port)
            if (success) {
                _uiState.update {
                    it.copy(
                        showConnectingDialog = false,
                        showOnlineModeDialog = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        showConnectingDialog = false,
                        showServerIpDialog = true,
                        connectionError = "Could not connect to server"
                    )
                }
            }
        }
    }

    private suspend fun makePingPong(ip: String, port: String): Boolean = withContext(Dispatchers.IO) {
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        // Using HTTP GET instead of WebSocket for the health check
        val request = Request.Builder()
            .url("http://$ip:$port")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: ""
                    body.contains("\"status\": \"alive\"") || body.contains("alive")
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}
