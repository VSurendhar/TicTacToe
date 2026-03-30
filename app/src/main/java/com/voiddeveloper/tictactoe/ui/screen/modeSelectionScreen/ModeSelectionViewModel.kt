package com.voiddeveloper.tictactoe.ui.screen.modeSelectionScreen

import android.util.Log
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
    val showServerUrlDialog: Boolean = false,
    val showConnectingDialog: Boolean = false,
    val showOnlineModeDialog: Boolean = false,
    val connectionError: String? = null,
    val serverUrl: String = "https://grovellingly-uninvocative-nguyet.ngrok-free.dev/"
)

class ModeSelectionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ModeSelectionUiState())
    val uiState: StateFlow<ModeSelectionUiState> = _uiState.asStateFlow()

    fun onLocalClick() {
        _uiState.update { it.copy(showLocalDialog = true) }
    }

    fun onRemoteClick() {
        _uiState.update { it.copy(showServerUrlDialog = true) }
    }

    fun onDismissLocalDialog() {
        _uiState.update { it.copy(showLocalDialog = false) }
    }

    fun onDismissServerUrlDialog() {
        _uiState.update { it.copy(showServerUrlDialog = false) }
    }

    fun onDismissOnlineModeDialog() {
        _uiState.update { it.copy(showOnlineModeDialog = false) }
    }

    fun onConfirmServerUrl(url: String) {
        _uiState.update {
            it.copy(
                showServerUrlDialog = false,
                showConnectingDialog = true,
                serverUrl = url,
                connectionError = null
            )
        }
        viewModelScope.launch {
            val success = makePingPong(url)
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
                        showServerUrlDialog = true,
                        connectionError = "Could not connect to server"
                    )
                }
            }
        }
    }

    private suspend fun makePingPong(url: String): Boolean = withContext(Dispatchers.IO) {
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                Log.i("Surendhar TAG", "makePingPong: $response")
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: ""
                    body.contains("\"status\": \"alive\"") || body.contains("alive")
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("Surendhar TAG", "makePingPong Error: ${e.message}")
            false
        }
    }
}
