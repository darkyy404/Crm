package com.example.proyectocrm.scenes


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectocrm.models.Contacto

@Composable
fun PantallaChat(navHostController: NavHostController, contacto: Contacto) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Chat con ${contacto.nombre}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Aquí puedes agregar la interfaz del chat (mensajes, caja de texto, etc.)
        Text(text = "Chat de ejemplo con ${contacto.nombre}")
    }
}
