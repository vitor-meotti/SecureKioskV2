package com.securekioskv2.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
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
fun DashboardScreen(onOpenDiagnostics: () -> Unit, onOpenAdminAuth: () -> Unit) {
    var taps by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Dashboard Kiosk")
        Text("Status: Inativo")
        Button(onClick = onOpenDiagnostics) { Text("Diagnóstico") }
        Button(onClick = { taps++ }) { Text("Área administrativa (7 toques)") }
        if (taps >= 7) {
            Text("Acesso admin detectado. Autenticação obrigatória.")
            Button(onClick = { taps = 0; onOpenAdminAuth() }) { Text("Autenticar admin") }
        }
    }
}
