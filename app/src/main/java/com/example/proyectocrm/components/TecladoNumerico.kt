package com.example.proyectocrm.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TecladoNumerico(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    buttonSize: Dp = 64.dp, // Tamaño configurable de los botones
    numberColor: Color = Color(0xFF1F1F1F), // Color de los números
    deleteButtonColor: Color = MaterialTheme.colorScheme.error, // Color del botón de borrar
    containerColor: Color = Color(0xFFF5F5F5) // Color de fondo del botón
) {
    val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "⌫")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        numbers.chunked(3).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
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
                                .size(buttonSize)
                                .padding(4.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = containerColor,
                                contentColor = if (number == "⌫") deleteButtonColor else numberColor
                            )
                        ) {
                            Text(
                                text = number,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(buttonSize)) // Espacio vacío para mantener la alineación
                    }
                }
            }
        }
    }
}
