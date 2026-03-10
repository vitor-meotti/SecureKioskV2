package com.securekioskv2.feature.adminauth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

@Composable
fun AdminAuthScreen(
    viewModel: AdminAuthViewModel,
    onAuthenticated: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var pin by remember { mutableStateOf("") }

    LaunchedEffect(state.lockoutRemainingMs) {
        if (state.lockoutRemainingMs > 0) {
            delay(1000)
            viewModel.refreshLockoutState()
        }
    }

    if (state.authenticated) {
        onAuthenticated()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Autenticação administrativa")
        OutlinedTextField(
            value = pin,
            onValueChange = { pin = it.filter(Char::isDigit).take(12) },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("PIN") }
        )
        Button(onClick = { viewModel.authenticate(pin) }) { Text("Entrar") }
        if (state.lockoutRemainingMs > 0) {
            Text("Bloqueado temporariamente (${state.lockoutRemainingMs} ms).")
        }
        Text("Tentativas falhas: ${state.failedAttempts}")
        Text(state.message)
        Button(onClick = onBack) { Text("Voltar") }
    }
}
