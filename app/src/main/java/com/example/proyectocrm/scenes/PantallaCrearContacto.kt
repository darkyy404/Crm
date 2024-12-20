package com.example.proyectocrm.scenes

import ContactosViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
        CustomTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(value = rol, onValueChange = { rol = it }, label = "Rol")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(value = email, onValueChange = { email = it }, label = "Correo Electrónico")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(value = telefono, onValueChange = { telefono = it }, label = "Teléfono")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(value = direccion, onValueChange = { direccion = it }, label = "Dirección")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(value = categoria, onValueChange = { categoria = it }, label = "Categoría")

        Spacer(modifier = Modifier.height(32.dp))

        // Botones
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(
                onClick = { navHostController.popBackStack() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White, // Fondo blanco
                    contentColor = Color(0xFF007BFF) // Letras en azul
                ),
                border = BorderStroke(1.dp, Color(0xFF007BFF)) // Bordes en azul
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)) // Botón de guardar azul
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(0xFF007BFF)) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White, // Fondo blanco
            focusedIndicatorColor = Color(0xFF007BFF), // Borde en azul al enfocar
            unfocusedIndicatorColor = Color(0xFF007BFF), // Borde en azul al desenfocar
            focusedLabelColor = Color(0xFF007BFF), // Texto del label en azul al enfocar
            cursorColor = Color(0xFF007BFF) // Color del cursor
        ),
        shape = RoundedCornerShape(8.dp) // Bordes redondeados
    )
}
