package com.voiddeveloper.tictactoe.data

import com.voiddeveloper.tictactoe.data.model.GameServerResponse
import com.voiddeveloper.tictactoe.domain.model.RemoteGameCommand
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class RemoteRepository(remoteGameCommand: RemoteGameCommand) {

    val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .pingInterval(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private lateinit var socket: WebSocket

    val json = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    private val request: Request = when (remoteGameCommand) {

        is RemoteGameCommand.JoinRoom -> {
            Request.Builder()
                .url("ws://localhost:8081/ticTacToe?action=join_room&roomId=${remoteGameCommand.roomId}")
                .build()
        }

        else -> {
            Request.Builder()
                .url("ws://localhost:8081/ticTacToe?action=create_room")
                .build()
        }

    }

    val serverResponseFlow = callbackFlow {
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("Connected!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val response = json.decodeFromString<GameServerResponse>(text)
                trySend(response)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                println("Closing: $code / $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("Closed: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("Error: ${t.message}")
            }
        }

        socket = client.newWebSocket(request, listener)

        awaitClose {
            socket.close(1000, "Flow closed")
            client.dispatcher.executorService.shutdown()
        }

    }

    fun sendMessage(message: String) {
        socket.send(message)
    }


}