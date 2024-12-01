package com.example.proyectocrm.scenes.acceso

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@Composable
fun PantallaAccesoSeguro(navHostController: NavHostController) {
    val context = LocalContext.current
    val savedPin = leerPreferencia(context, "user_pin") // Leer PIN almacenado
    var pinInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEDF1F3), Color(0xFFFFFFFF)) // Ajustar colores
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Título
        Text(
            text = "Ingresa tu PIN",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF1F1F1F)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campos para mostrar el PIN ingresado
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(4) { index ->
                val char = if (index < pinInput.length) "●" else ""
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(4.dp)
                        .background(Color(0xFFEDF1F3), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007AFF) // Color principal de la app
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Teclado numérico
        TecladoNumerico(
            onNumberClick = { number ->
                if (pinInput.length < 4) {
                    pinInput += number
                }
                if (pinInput.length == 4) {
                    if (pinInput == savedPin) {
                        navHostController.navigate("pantallaHome")
                    } else {
                        errorMessage = "PIN incorrecto"
                        pinInput = "" // Reiniciar PIN ingresado
                    }
                }
            },
            onDeleteClick = {
                if (pinInput.isNotEmpty()) {
                    pinInput = pinInput.dropLast(1)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Componente del teclado numérico
@Composable
fun TecladoNumerico(onNumberClick: (String) -> Unit, onDeleteClick: () -> Unit) {
    val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "⌫")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        numbers.chunked(3).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                row.forEach { number ->
                    if (number.isNotEmpty()) {
                        Button(
                            onClick = {
                                if (number == "⌫") {
                                    onDeleteClick()
                                } else {
                                    onNumberClick(number)
                                }
                            },
                            modifier = Modifier
                                .size(64.dp)
                                .padding(4.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF5F5F5),
                                contentColor = Color(0xFF1F1F1F)
                            )
                        ) {
                            Text(
                                text = number,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (number == "⌫") MaterialTheme.colorScheme.error else Color(0xFF1F1F1F)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(64.dp)) // Espacio vacío
                    }
                }
            }
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
