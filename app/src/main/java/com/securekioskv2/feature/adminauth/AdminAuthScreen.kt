package com.securekioskv2.feature.adminauth

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

@Composable
fun AdminAuthScreen(onAuthenticated: () -> Unit, vm: AdminAuthViewModel = hiltViewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    var pin by remember { mutableStateOf("") }
    LaunchedEffect(state.lockoutRemainingMs) {
        if (state.lockoutRemainingMs > 0) { delay(1000); vm.refreshLockout() }
    }
    if (state.authenticated) onAuthenticated()
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        OutlinedTextField(pin, { pin = it.filter(Char::isDigit).take(12) }, label={Text("PIN")}, visualTransformation = PasswordVisualTransformation())
        Button(onClick = {
            val chars = pin.toCharArray()
            vm.authenticate(chars)
        }) { Text("Entrar") }
        Text("Falhas: ${state.failedAttempts}")
        if (state.lockoutRemainingMs > 0) Text("Bloqueio: ${state.lockoutRemainingMs} ms")
        if (state.message.isNotBlank()) Text(state.message)
    }
}
