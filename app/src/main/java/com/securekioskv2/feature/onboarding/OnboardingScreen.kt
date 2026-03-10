package com.securekioskv2.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.securekioskv2.core.security.AdminCredentialStore

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    var pin by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SecureKioskV2", style = MaterialTheme.typography.headlineMedium)
        Text("Configuração inicial segura e consentida.")

        OutlinedTextField(
            value = pin,
            onValueChange = { pin = it.filter(Char::isDigit).take(12) },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Defina PIN admin (mínimo 6)") }
        )
        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it.filter(Char::isDigit).take(12) },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Confirmar PIN") }
        )

        Button(onClick = {
            if (pin.length < 6 || pin != confirm) {
                message = "PIN inválido ou confirmação diferente."
            } else {
                AdminCredentialStore.configure(pin)
                onContinue()
            }
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text("Continuar")
        }

        if (message.isNotBlank()) Text(message)
    }
}
