package com.voiddeveloper.tictactoe.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.voiddeveloper.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun ServerIpDialog(
    onConfirm: (ip: String, port: String) -> Unit,
    onDismiss: () -> Unit,
    initialIp: String = "",
    initialPort: String = "8081",
) {
    var ipAddress by remember { mutableStateOf(initialIp) }
    var port by remember { mutableStateOf(initialPort) }
    var ipError by remember { mutableStateOf<String?>(null) }
    var portError by remember { mutableStateOf<String?>(null) }

    val portFocusRequester = remember { FocusRequester() }

    fun validate(): Boolean {
        var valid = true

        // Basic IPv4 validation
        val ipRegex = Regex(
            "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
        )
        if (ipAddress.isBlank()) {
            ipError = "IP address is required"
            valid = false
        } else if (!ipRegex.matches(ipAddress.trim())) {
            ipError = "Enter a valid IP (e.g. 192.168.1.36)"
            valid = false
        } else {
            ipError = null
        }

        val portInt = port.toIntOrNull()
        if (port.isBlank()) {
            portError = "Port is required"
            valid = false
        } else if (portInt == null || portInt !in 1..65535) {
            portError = "Port must be between 1 and 65535"
            valid = false
        } else {
            portError = null
        }

        return valid
    }


    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Title
                Text(
                    text = "Server Connection",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                // Subtitle
                Text(
                    text = "Enter the IP address of the machine running the game server.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // IP Address field
                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = {
                        ipAddress = it
                        ipError = null
                    },
                    label = { Text("IP Address") },
                    placeholder = { Text("e.g. 192.168.1.36") },
                    isError = ipError != null,
                    supportingText = {
                        if (ipError != null) {
                            Text(
                                text = ipError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { portFocusRequester.requestFocus() }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Port field
                OutlinedTextField(
                    value = port,
                    onValueChange = {
                        port = it
                        portError = null
                    },
                    label = { Text("Port") },
                    placeholder = { Text("8081") },
                    isError = portError != null,
                    supportingText = {
                        if (portError != null) {
                            Text(
                                text = portError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (validate()) onConfirm(ipAddress.trim(), port.trim())
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(portFocusRequester),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Connect button
                Button(
                    onClick = {
                        if (validate()) onConfirm(ipAddress.trim(), port.trim())
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Connect",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Dismiss link
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewServerIpDialog() {
    TicTacToeTheme {
        ServerIpDialog({ _, _ -> }, {})
    }
}
