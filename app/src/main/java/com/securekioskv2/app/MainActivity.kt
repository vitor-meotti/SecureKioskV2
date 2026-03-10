package com.securekioskv2.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.securekioskv2.core.kiosk.KioskLockTaskManager
import com.securekioskv2.feature.adminauth.AdminAuthScreen
import com.securekioskv2.feature.dashboard.DashboardScreen
import com.securekioskv2.feature.diagnostics.DiagnosticsScreen
import com.securekioskv2.feature.onboarding.OnboardingScreen
import com.securekioskv2.ui.theme.SecureKioskTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var kioskLockTaskManager: KioskLockTaskManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val sessionVm: AppSessionViewModel = hiltViewModel()
            SecureKioskTheme {
                Surface {
                    NavHost(navController, startDestination = sessionVm.startDestination()) {
                        composable("onboarding") {
                            OnboardingScreen(
                                onContinue = {
                                    navController.navigate("dashboard") {
                                        popUpTo("onboarding") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        composable("dashboard") { DashboardScreen({ navController.navigate("admin") }, { navController.navigate("diagnostics") }) }
                        composable("admin") {
                            AdminAuthScreen(
                                onAuthenticated = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("diagnostics") { DiagnosticsScreen() }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        kioskLockTaskManager.startIfPermitted(this)
    }
}
