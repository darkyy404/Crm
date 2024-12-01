package com.example.proyectocrm.scenes.acceso

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.proyectocrm.components.guardarPreferencia
import com.example.proyectocrm.components.TecladoNumerico

@Composable
fun PantallaConfigurarPin(navHostController: NavHostController) {
    val context = LocalContext.current
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isConfirming by remember { mutableStateOf(false) }

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
            text = if (isConfirming) "Confirma tu PIN" else "Introduce tu nuevo PIN",
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
                            color = if (index < pin.length) Color.White else Color.LightGray,
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
                        if (pin.length < 4) {
                            pin += number
                        }
                        if (pin.length == 4 && isConfirming) {
                            if (pin == confirmPin) {
                                guardarPreferencia(context, "user_pin", pin)
                                navHostController.navigate("pantallaPerfil")
                            } else {
                                errorMessage = "Los PIN ingresados no coinciden."
                                pin = ""
                                confirmPin = ""
                                isConfirming = false
                            }
                        } else if (pin.length == 4 && !isConfirming) {
                            confirmPin = pin
                            pin = ""
                            isConfirming = true
                        }
                    },
                    onDeleteClick = {
                        if (pin.isNotEmpty()) {
                            pin = pin.dropLast(1)
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
