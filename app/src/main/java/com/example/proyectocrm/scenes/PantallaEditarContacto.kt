package com.example.proyectocrm.scenes

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun PantallaEditarContacto(
    navHostController: NavHostController,
    contacto: Contacto,
    onSave: (Contacto) -> Unit,
    onCancel: () -> Unit
) {
    var nombre by remember { mutableStateOf(contacto.nombre) }
    var rol by remember { mutableStateOf(contacto.rol) }
    var email by remember { mutableStateOf(contacto.email) }
    var telefono by remember { mutableStateOf(contacto.telefono) }
    var direccion by remember { mutableStateOf(contacto.direccion) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Editar Contacto") },
                navigationIcon = {
                    IconButton(onClick = { onCancel() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Cancelar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Campos de edición
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF007AFF),
                    unfocusedIndicatorColor = Color(0xFF007AFF),
                    cursorColor = Color(0xFF007AFF)
                )
            )

            TextField(
                value = rol,
                onValueChange = { rol = it },
                label = { Text("Rol (e.g., Diseñador UI/UX)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF007AFF),
                    unfocusedIndicatorColor = Color(0xFF007AFF),
                    cursorColor = Color(0xFF007AFF)
                )
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF007AFF),
                    unfocusedIndicatorColor = Color(0xFF007AFF),
                    cursorColor = Color(0xFF007AFF)
                )
            )

            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF007AFF),
                    unfocusedIndicatorColor = Color(0xFF007AFF),
                    cursorColor = Color(0xFF007AFF)
                )
            )

            TextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF007AFF),
                    unfocusedIndicatorColor = Color(0xFF007AFF),
                    cursorColor = Color(0xFF007AFF)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botones
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onCancel() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                ) {
                    Text("Cancelar", color = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        val contactoActualizado = Contacto(
                            nombre = nombre,
                            rol = rol,
                            email = email,
                            telefono = telefono,
                            direccion = direccion,
                            ultimoMensaje = contacto.ultimoMensaje
                        )
                        onSave(contactoActualizado) // Guardar cambios
                        navHostController.popBackStack()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                ) {
                    Text("Guardar", color = Color.White)
                }
            }
        }
    }
}

