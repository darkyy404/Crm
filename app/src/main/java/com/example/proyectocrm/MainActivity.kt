package com.example.proyectocrm
import android.Manifest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.proyectocrm.scripts.DataImporter

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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

            // Mostrar contenido principal
            if (isPermissionGranted.value) {
                NavigationWrapper(navHostController)
            }
        }
    }
}
