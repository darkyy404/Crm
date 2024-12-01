package com.example.proyectocrm.scenes.acceso

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectocrm.components.guardarPreferencia

@Composable
fun PantallaConfigurarPin(navHostController: NavHostController) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var isConfirming by remember { mutableStateOf(false) } // Indica si está en modo confirmar PIN
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current // Capturamos el contexto dentro del @Composable

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEDF1F3), Color(0xFFFFFFFF))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Título dinámico según el estado
        Text(
            text = if (isConfirming) "Confirma tu PIN" else "Introduce tu nuevo PIN",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F1F1F)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campos para mostrar el PIN ingresado
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            val currentPin = if (isConfirming) confirmPin else pin
            repeat(4) { index ->
                val char = if (index < currentPin.length) currentPin[index].toString() else ""
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(4.dp)
                        .background(Color(0xFFEDF1F3), shape = MaterialTheme.shapes.medium),
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
                if (isConfirming) {
                    if (confirmPin.length < 4) confirmPin += number
                } else {
                    if (pin.length < 4) pin += number
                }

                if (pin.length == 4 && isConfirming && confirmPin.length == 4) {
                    if (pin == confirmPin) {
                        guardarPreferencia(context, "user_pin", pin) // Usamos el contexto capturado
                        navHostController.navigate("pantallaAccesoSeguro") // Redirige al acceso seguro
                    } else {
                        errorMessage = "Los PIN ingresados no coinciden."
                        pin = ""
                        confirmPin = ""
                        isConfirming = false
                    }
                } else if (pin.length == 4 && !isConfirming) {
                    isConfirming = true
                }
            },
            onDeleteClick = {
                if (isConfirming) {
                    if (confirmPin.isNotEmpty()) confirmPin = confirmPin.dropLast(1)
                } else {
                    if (pin.isNotEmpty()) pin = pin.dropLast(1)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error
        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}
