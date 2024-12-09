package com.example.proyectocrm.scenes

import ContactosViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectocrm.models.Contacto
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarContacto(
    navHostController: NavHostController,
    viewModel: ContactosViewModel
) {
    val contactoSeleccionado by viewModel.contactoSeleccionado.collectAsState()

    if (contactoSeleccionado == null) {
        Text("No hay contacto seleccionado para editar.")
        return
    }

    var nombre by remember { mutableStateOf(contactoSeleccionado!!.nombre) }
    var rol by remember { mutableStateOf(contactoSeleccionado!!.rol) }
    var email by remember { mutableStateOf(contactoSeleccionado!!.email) }
    var telefono by remember { mutableStateOf(contactoSeleccionado!!.telefono) }
    var direccion by remember { mutableStateOf(contactoSeleccionado!!.direccion) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Editar Contacto",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos del formulario
        CustomTextFieldEditarContacto(value = nombre, onValueChange = { nombre = it }, label = "Nombre")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextFieldEditarContacto(value = rol, onValueChange = { rol = it }, label = "Rol")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextFieldEditarContacto(value = email, onValueChange = { email = it }, label = "Correo Electrónico")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextFieldEditarContacto(value = telefono, onValueChange = { telefono = it }, label = "Teléfono")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextFieldEditarContacto(value = direccion, onValueChange = { direccion = it }, label = "Dirección")

        Spacer(modifier = Modifier.height(32.dp))

        // Botones
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(
                onClick = { navHostController.popBackStack() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF007BFF)
                ),
                border = BorderStroke(1.dp, Color(0xFF007BFF))
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    val contactoActualizado = contactoSeleccionado!!.copy(
                        nombre = nombre,
                        rol = rol,
                        email = email,
                        telefono = telefono,
                        direccion = direccion
                    )
                    viewModel.actualizarContacto(contactoActualizado)
                    viewModel.limpiarSeleccion()
                    navHostController.popBackStack()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFieldEditarContacto(value: String, onValueChange: (String) -> Unit, label: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color(0xFF007BFF),
            unfocusedIndicatorColor = Color(0xFF007BFF),
            cursorColor = Color(0xFF007BFF)
        )
    )
}
