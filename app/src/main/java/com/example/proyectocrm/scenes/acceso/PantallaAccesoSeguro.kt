package com.example.proyectocrm.scenes.acceso

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@Composable
fun PantallaAccesoSeguro(navHostController: NavHostController) {
    val context = LocalContext.current
    var pinInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var failedAttempts by remember { mutableStateOf(0) } // Contador de intentos fallidos
    val maxAttempts = 5 // Límite máximo de intentos fallidos

    // Interfaz para autenticación por PIN
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Introduce tu PIN para acceder", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = pinInput,
            onValueChange = { pinInput = it },
            label = { Text("PIN") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                val savedPin = leerPreferencia(context, "user_pin")
                if (pinInput == savedPin) {
                    navHostController.navigate("PantallaHome") // Navega a la pantalla principal si el PIN es correcto
                } else {
                    failedAttempts += 1
                    errorMessage = "PIN incorrecto"
                    if (failedAttempts >= maxAttempts) {
                        navHostController.navigate("PantallaAccesoFallido") // Navega a la pantalla de intentos fallidos
                    }
                }
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Acceder")
        }
    }
}

// Leer datos de configuración desde almacenamiento cifrado
fun leerPreferencia(context: Context, key: String): String? {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_preferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    return sharedPreferences.getString(key, null)
}
