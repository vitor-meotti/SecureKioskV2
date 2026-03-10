package com.securekioskv2.feature.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingScreen(onContinue: () -> Unit, vm: OnboardingViewModel = hiltViewModel()) {
    var pin by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        OutlinedTextField(pin, { pin = it.filter(Char::isDigit).take(12) }, label={Text("PIN admin")}, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(confirm, { confirm = it.filter(Char::isDigit).take(12) }, label={Text("Confirmar PIN")}, visualTransformation = PasswordVisualTransformation())
        Button(onClick = {
            val pinChars = pin.toCharArray(); val confirmChars = confirm.toCharArray()
            if (pinChars.size < 6 || !pinChars.contentEquals(confirmChars)) {
                pinChars.fill('\u0000'); confirmChars.fill('\u0000'); message = "PIN inválido"
            } else {
                vm.savePin(pinChars); confirmChars.fill('\u0000'); onContinue()
            }
        }) { Text("Salvar e continuar") }
        if (message.isNotBlank()) Text(message)
    }
}
