package com.securekioskv2.feature.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(openAdmin: () -> Unit, openDiagnostics: () -> Unit) {
    var taps by remember { mutableStateOf(0) }
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Dashboard")
        Button(onClick = openDiagnostics) { Text("Diagnóstico") }
        Button(onClick = { taps++ }) { Text("Área admin") }
        if (taps >= 7) Button(onClick = { taps = 0; openAdmin() }) { Text("Autenticar admin") }
    }
}
