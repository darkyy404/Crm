package com.example.proyectocrm.scenes

import ContactosViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Importar Color correctamente
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectocrm.models.Contacto // Importar Contacto correctamente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearContacto(
    navHostController: NavHostController,
    viewModel: ContactosViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var ultimoMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Contacto",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = ultimoMensaje,
            onValueChange = { ultimoMensaje = it },
            label = { Text("Mensaje Inicial") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navHostController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    val nuevoContacto = Contacto(nombre, ultimoMensaje)
                    viewModel.agregarContacto(nuevoContacto) // Llama al método para agregar el contacto
                    navHostController.popBackStack() // Regresa a la pantalla anterior
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)) // Color para el botón
            ) {
                Text("Guardar")
            }
        }
    }
}
