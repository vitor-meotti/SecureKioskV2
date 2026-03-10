package com.securekioskv2.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.securekioskv2.feature.adminauth.AdminAuthScreen
import com.securekioskv2.feature.adminauth.AdminAuthViewModel
import com.securekioskv2.feature.dashboard.DashboardScreen
import com.securekioskv2.feature.diagnostics.DiagnosticsScreen
import com.securekioskv2.feature.onboarding.OnboardingScreen
import com.securekioskv2.ui.theme.SecureKioskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppRoot() }
    }
}

@Composable
private fun AppRoot() {
    val navController = rememberNavController()
    val appSessionViewModel: AppSessionViewModel = viewModel()

    SecureKioskTheme {
        Surface {
            NavHost(
                navController = navController,
                startDestination = appSessionViewModel.startDestination()
            ) {
                composable("onboarding") {
                    OnboardingScreen(
                        onContinue = {
                            navController.navigate("dashboard") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    )
                }
                composable("dashboard") {
                    DashboardScreen(
                        onOpenDiagnostics = { navController.navigate("diagnostics") },
                        onOpenAdminAuth = { navController.navigate("admin-auth") }
                    )
                }
                composable("admin-auth") {
                    val adminAuthViewModel: AdminAuthViewModel = viewModel()
                    AdminAuthScreen(
                        viewModel = adminAuthViewModel,
                        onAuthenticated = { navController.popBackStack() },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("diagnostics") {
                    DiagnosticsScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}
