package com.voiddeveloper.tictactoe.data

import android.util.Log
import com.voiddeveloper.tictactoe.data.model.GameServerResponse
import com.voiddeveloper.tictactoe.data.model.RemoteGameStatus
import com.voiddeveloper.tictactoe.domain.model.RemoteGameCommand
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

private const val TAG = "RemoteRepository"

class RemoteRepository() {

    val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .pingInterval(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private lateinit var socket: WebSocket
    lateinit var serverResponseFlow: Flow<GameServerResponse>
    var isClosed: Boolean = false

    val json = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    private lateinit var request: Request

    fun init(remoteGameCommand: RemoteGameCommand) {
        Log.d(TAG, "Initializing with command: $remoteGameCommand")

        request = when (remoteGameCommand) {
            is RemoteGameCommand.JoinRoom -> {
                Request.Builder()
                    .url("ws://${remoteGameCommand.serverIp}:${remoteGameCommand.serverPort}/ticTacToe?action=join_room&roomId=${remoteGameCommand.roomId}")
                    .build()
            }

            is RemoteGameCommand.CreateRoom -> {
                Request.Builder()
                    .url("ws://${remoteGameCommand.serverIp}:${remoteGameCommand.serverPort}/ticTacToe?action=create_room")
                    .build()
            }

            is RemoteGameCommand.ReconnectionAttempt -> {
                Request.Builder()
                    .url("ws://${remoteGameCommand.serverIp}:${remoteGameCommand.serverPort}/ticTacToe?action=reconnection_attempt?userId=${remoteGameCommand.userId}&roomId=${remoteGameCommand.roomId}&assignedChar=${remoteGameCommand.assignedChar}")
                    .build()
            }

        }

        Log.d(TAG, "Request URL: ${request.url}")

        serverResponseFlow = callbackFlow {
            val listener = object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    Log.i(TAG, "WebSocket Connected!")
                    isClosed = false
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.d(TAG, "Message received: $text")
                    try {
                        val response = json.decodeFromString<GameServerResponse>(text)
                        trySend(response)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to decode message", e)
                    }
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.w(TAG, "WebSocket Closing: $code / $reason")
                    webSocket.close(1000, null)
                    isClosed = true
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    Log.w(TAG, "WebSocket Closed: $code / $reason")
                    trySend(GameServerResponse(message = RemoteGameStatus.GameDisConnected))
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e(TAG, "WebSocket Error: ${t.message}", t)
                    trySend(GameServerResponse(message = RemoteGameStatus.GameDisConnected))
                }
            }

            socket = client.newWebSocket(request, listener)

            awaitClose {
                Log.d(TAG, "Flow closed, closing WebSocket")
                isClosed = true
                socket.close(1000, "Flow closed")
                client.dispatcher.executorService.shutdown()
            }

        }

    }

    fun sendMessage(message: String) {
        Log.d(TAG, "Sending message: $message")
        socket.send(message)
    }


}
