package com.example.proyectocrm

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.proyectocrm.scripts.DataImporter
import com.example.proyectocrm.ui.theme.ProyectoCrmTheme

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoCrmTheme {
                navHostController = rememberNavController()

                // Solicitar permisos necesarios al inicio
                val isPermissionGranted = remember { mutableStateOf(false) }
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        isPermissionGranted.value = isGranted
                    }
                )

                // Lanzar solicitud de permisos
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }

                // Navegación y contenido principal
                if (isPermissionGranted.value) {
                    NavigationWrapper(navHostController)
                } else {
                    RequestPermissionScreen(permissionLauncher)
                }
            }
        }
    }
}

// Pantalla para solicitar permiso
@Composable
fun RequestPermissionScreen(permissionLauncher: ManagedActivityResultLauncher<String, Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "La aplicación necesita permisos para continuar.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
            Text(text = "Solicitar permisos")
        }
    }
}
