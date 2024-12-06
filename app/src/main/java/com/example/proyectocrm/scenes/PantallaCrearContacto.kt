package com.example.proyectocrm.scenes

import ContactosViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectocrm.models.Contacto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearContacto(
    navHostController: NavHostController,
    viewModel: ContactosViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Crear Contacto",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos del formulario
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = rol, onValueChange = { rol = it }, label = { Text("Rol") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })

        Spacer(modifier = Modifier.height(32.dp))

        // Botones
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { navHostController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    // Crear el contacto
                    val nuevoContacto = Contacto(
                        nombre = nombre,
                        ultimoMensaje = "Nuevo contacto añadido",
                        rol = rol,
                        email = email,
                        telefono = telefono,
                        direccion = direccion,
                    )
                    viewModel.agregarContacto(nuevoContacto) // Agregar al ViewModel
                    navHostController.popBackStack() // Regresar
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            ) {
                Text("Guardar")
            }
        }
    }
}

