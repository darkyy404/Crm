package com.example.proyectocrm.scenes.acceso

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@Composable
fun PantallaAccesoSeguro(navHostController: NavHostController) {
    val context = LocalContext.current
    val authMethod = leerPreferencia(context, "auth_method")
    var pinInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var dialogState by remember { mutableStateOf<Pair<Boolean, String>?>(null) }


    when (authMethod) {
        "fingerprint" -> {
            val biometricPrompt = BiometricPrompt(
                context as androidx.fragment.app.FragmentActivity,
                ContextCompat.getMainExecutor(context),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        navHostController.navigate("PantallaPrincipal")
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        dialogState = Pair(false, "Error de autenticación: $errString")
                    }

                    override fun onAuthenticationFailed() {
                        dialogState = Pair(false, "Autenticación fallida.")
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Acceso Seguro con Huella")
                .setSubtitle("Confirma tu identidad")
                .setNegativeButtonText("Cancelar")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
        "pin" -> {
            // Interfaz para autenticación por PIN
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Introduce tu PIN para acceder", fontSize = 20.sp)

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

                Button(onClick = {
                    val savedPin = leerPreferencia(context, "user_pin")
                    if (pinInput == savedPin) {
                        navHostController.navigate("pantallaPrincipal")
                    } else {
                        errorMessage = "PIN incorrecto"
                    }
                }) {
                    Text("Acceder")
                }
            }
        }
        else -> {
            navHostController.navigate("pantallaLogin")
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
