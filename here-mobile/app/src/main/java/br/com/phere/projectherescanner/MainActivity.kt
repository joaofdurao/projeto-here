package br.com.phere.projectherescanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.phere.projectherescanner.data.api.RetrofitInstance
import br.com.phere.projectherescanner.ui.screens.*
import br.com.phere.projectherescanner.viewmodel.AuthViewModel
import br.com.phere.projectherescanner.viewmodel.EventosViewModel
import br.com.phere.projectherescanner.ui.theme.ProjectHereScannerTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Event
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import br.com.phere.projectherescanner.data.auth.LoginState

class MainActivity : ComponentActivity() {

    private val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            showPermissionDeniedMessage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)
        val isAuthenticated = token != null

        if (isAuthenticated) {
            RetrofitInstance.setAuthToken(token!!)
        }

        checkCameraPermission()

        setContent {
            ProjectHereScannerTheme {
                val navController = rememberNavController()
                val eventosViewModel: EventosViewModel = viewModel()
                val authViewModel: AuthViewModel = viewModel()
                var currentScreen by remember { mutableStateOf("login") }
                var authenticated by remember { mutableStateOf(isAuthenticated) }

                LaunchedEffect(authViewModel.loginState.collectAsState().value) {
                    if (authViewModel.loginState.value is LoginState.Success) {
                        authenticated = true
                        navController.navigate("eventosList") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }

                Scaffold(
                    floatingActionButton = {
                        if (authenticated && (currentScreen == "eventosList" || currentScreen == "eventosFinalizados")) {
                            FloatingActionButton(
                                onClick = { navController.navigate("createEvent") },
                                content = {
                                    Icon(Icons.Default.Add, contentDescription = "Criar Novo Evento")
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (authenticated) {
                            BottomNavigationBar(
                                navController = navController,
                                currentScreen = currentScreen,
                                onScreenSelected = { screen -> currentScreen = screen }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (authenticated) "eventosList" else "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            currentScreen = "login"
                            LoginScreen(viewModel = authViewModel, navController = navController)
                        }
                        composable("eventosList") {
                            currentScreen = "eventosList"
                            EventosListScreen(viewModel = eventosViewModel, navController = navController)
                        }
                        composable("eventosFinalizados") {
                            currentScreen = "eventosFinalizados"
                            EventosFinalizadosListScreen(viewModel = eventosViewModel, navController = navController)
                        }
                        composable(
                            route = "eventoDetalhes/{eventoId}",
                            arguments = listOf(navArgument("eventoId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            currentScreen = "eventoDetalhes"
                            val eventoId = backStackEntry.arguments?.getString("eventoId") ?: ""
                            EventoDetalhesScreen(eventoId = eventoId, viewModel = eventosViewModel)
                        }
                        composable("qrCodeScanner") {
                            currentScreen = "qrCodeScanner"
                            QRCodeScannerScreen(navController = navController, viewModel = eventosViewModel)
                        }
                        composable("createEvent") {
                            currentScreen = "createEvent"
                            CreateEventScreen(navController = navController, viewModel = eventosViewModel)
                        }
                    }
                }
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showPermissionDeniedMessage() {
        Toast.makeText(this, "Permissão de câmera é necessária para escanear QR codes", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun BottomNavigationBar(
    navController: androidx.navigation.NavHostController,
    currentScreen: String,
    onScreenSelected: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == "eventosList",
            onClick = {
                onScreenSelected("eventosList")
                navController.navigate("eventosList") { launchSingleTop = true }
            },
            icon = { Icon(Icons.Default.Event, contentDescription = "Eventos Ativos") },
            label = { Text("Ativos") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("qrCodeScanner") },
            icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = "Escanear QR Code") },
            label = { Text("Scanner") }
        )
        NavigationBarItem(
            selected = currentScreen == "eventosFinalizados",
            onClick = {
                onScreenSelected("eventosFinalizados")
                navController.navigate("eventosFinalizados") { launchSingleTop = true }
            },
            icon = { Icon(Icons.Default.Checklist, contentDescription = "Eventos Finalizados") },
            label = { Text("Finalizados") }
        )
    }
}
