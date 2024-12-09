package com.example.proyectocrm.scenes.acceso

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.proyectocrm.components.TecladoNumerico

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
                    colors = listOf(
                        Color(0xFF007AFF), // Azul arriba
                        Color.White // Blanco abajo
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espaciado superior
        Spacer(modifier = Modifier.height(40.dp))

        // Título
        Text(
            text = "Ingresa tu PIN",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Indicadores visuales del PIN
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 60.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(6.dp)
                        .background(
                            color = if (index < pinInput.length) Color.White else Color.LightGray,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }

        // Espaciado entre los indicadores y el teclado
        Spacer(modifier = Modifier.height(32.dp))

        // Fondo blanco detrás del teclado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, top = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Teclado numérico reutilizado
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
                    },
                    buttonSize = 78.dp, // Ajuste del tamaño de los botones
                    numberColor = Color(0xFF1F1F1F), // Color de los números
                    deleteButtonColor = MaterialTheme.colorScheme.error // Color del botón de borrar
                )
            }
        }

        // Mensaje de error
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
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
