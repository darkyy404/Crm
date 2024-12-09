package com.example.proyectocrm.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectocrm.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaRecuperarContrasena(navHostController: NavHostController) {
    val auth = FirebaseAuth.getInstance() // Instancia de FirebaseAuth
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val message = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0052D4), // Azul oscuro
                        Color(0xFF4364F7), // Azul medio
                        Color(0xFF6FB1FC)  // Azul claro
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart) // Alineación en la parte superior izquierda
                .padding(16.dp), // Ajuste de espacio
        ) {
            // Botón de volver atrás
            IconButton(
                onClick = { navHostController.popBackStack() }, // Regresa a la pantalla anterior
                modifier = Modifier.size(48.dp) // Tamaño del botón
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono o logo
            Icon(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                text = "¿Olvidaste la contraseña?",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo
            Text(
                text = "Introduce el email para recuperarla",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de email
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                placeholder = { Text("alejandro@example.com", color = Color.White.copy(alpha = 0.7f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de recuperación
            Button(
                onClick = {
                    isLoading.value = true
                    enviarCorreoRecuperacionSeguro(
                        auth,
                        email.value.text,
                        message,
                        isLoading
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading.value
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Enviar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de estado
            if (message.value.isNotEmpty()) {
                Text(
                    text = message.value,
                    color = if (message.value.startsWith("Error")) Color.Red else Color.Green,
                    fontSize = 14.sp
                )
            }
        }
    }
}


// Función para enviar el correo de recuperación
fun enviarCorreoRecuperacionSeguro(
    auth: FirebaseAuth,
    email: String,
    message: MutableState<String>,
    isLoading: MutableState<Boolean>
) {
    if (email.isBlank()) {
        message.value = "Por favor, ingresa un correo válido."
        isLoading.value = false
        return
    }

    // Envía directamente el correo de restablecimiento
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                message.value = "Correo de recuperación enviado exitosamente."
            } else {
                // Firebase no revela si el correo existe por motivos de seguridad
                message.value = "Si el correo está registrado, recibirás un enlace de recuperación."
            }
            isLoading.value = false
        }
        .addOnFailureListener { e ->
            message.value = "Error: ${e.localizedMessage}"
            isLoading.value = false
        }
}

