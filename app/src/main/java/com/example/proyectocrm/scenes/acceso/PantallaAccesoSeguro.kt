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
    var failedAttempts by remember { mutableStateOf(0) } // Contador de intentos fallidos
    val maxAttempts = 5 // Límite máximo de intentos fallidos

    when (authMethod) {
        "fingerprint" -> {
            // Instancia de BiometricPrompt para manejar la autenticación biométrica
            val biometricPrompt = BiometricPrompt(
                context as androidx.fragment.app.FragmentActivity, // El contexto debe ser una actividad Fragment
                ContextCompat.getMainExecutor(context), // Executor para manejar callbacks en el hilo principal
                object : BiometricPrompt.AuthenticationCallback() { // Callbacks para manejar los eventos de autenticación

                    // Llamado cuando la autenticación es exitosa
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        // Redirige al usuario a la pantalla principal de la aplicación
                        navHostController.navigate("PantallaHome")
                    }

                    // Llamado cuando ocurre un error en la autenticación
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        // Incrementa el contador de intentos fallidos
                        failedAttempts += 1
                        // Muestra un diálogo con el mensaje de error
                        dialogState = Pair(false, "Error de autenticación: $errString")
                        // Si el número de intentos fallidos alcanza el máximo permitido, redirige a PantallaAccesoFallido
                        if (failedAttempts >= maxAttempts) {
                            navHostController.navigate("PantallaAccesoFallido")
                        }
                    }

                    // Llamado cuando la autenticación falla pero no es un error crítico
                    override fun onAuthenticationFailed() {
                        // Incrementa el contador de intentos fallidos
                        failedAttempts += 1
                        // Muestra un mensaje indicando que la autenticación falló
                        dialogState = Pair(false, "Autenticación fallida.")
                        // Si el número de intentos fallidos alcanza el máximo permitido, redirige a PantallaAccesoFallido
                        if (failedAttempts >= maxAttempts) {
                            navHostController.navigate("PantallaAccesoFallido")
                        }
                    }
                }
            )

            // Configuración de los parámetros para el cuadro de diálogo de autenticación biométrica
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Acceso Seguro con Huella") // Título que se mostrará en el cuadro de diálogo
                .setSubtitle("Confirma tu identidad") // Subtítulo del cuadro de diálogo
                .setNegativeButtonText("Cancelar") // Texto del botón para cancelar la autenticación
                .build()

            // Inicia el proceso de autenticación biométrica
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
                        navHostController.navigate("PantallaHome")
                    } else {
                        failedAttempts += 1
                        errorMessage = "PIN incorrecto"
                        if (failedAttempts >= maxAttempts) {
                            navHostController.navigate("PantallaAccesoFallido")
                        }
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

    // Mostrar diálogos según el estado de autenticación
    dialogState?.let { (success, message) ->
        AlertDialog(
            onDismissRequest = { dialogState = null },
            title = { Text(if (success) "¡Éxito!" else "Error") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { dialogState = null }) {
                    Text("OK")
                }
            }
        )
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
