package com.example.proyectocrm.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectocrm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarPerfil(navHostController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    // Estados para los datos del usuario
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val phone = remember { mutableStateOf(TextFieldValue("")) }
    val message = remember { mutableStateOf("") }

    // Efecto para cargar datos de Firestore
    LaunchedEffect(Unit) {
        currentUser?.let { user ->
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    name.value = TextFieldValue(document.getString("name") ?: "")
                    email.value = TextFieldValue(document.getString("email") ?: "")
                    phone.value = TextFieldValue(document.getString("phone") ?: "")
                }
                .addOnFailureListener {
                    message.value = "Error al cargar datos: ${it.message}"
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F3))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barra superior con ícono de volver y guardar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier
                    .clickable { navHostController.popBackStack() }
                    .size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Guardar",
                modifier = Modifier
                    .clickable {
                        guardarDatosEnFirestore(currentUser?.uid, name.value.text, email.value.text, phone.value.text, message)
                    }
                    .size(24.dp),
                tint = Color(0xFF007AFF)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de perfil
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.LightGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile_placeholder),
                contentDescription = "Imagen de perfil",
                modifier = Modifier.size(60.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campos editables
        EditProfileField("Nombre", name.value, onValueChange = { name.value = it })
        EditProfileField("Correo electrónico", email.value, onValueChange = { email.value = it }, keyboardType = KeyboardType.Email)
        EditProfileField("Número telefónico", phone.value, onValueChange = { phone.value = it }, keyboardType = KeyboardType.Phone)

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de estado
        if (message.value.isNotEmpty()) {
            Text(
                text = message.value,
                color = if (message.value.startsWith("Error")) Color.Red else Color.Green,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Etiqueta del campo
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Campo de entrada
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            placeholder = { Text(label, color = Color.Gray) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color(0xFF007AFF),
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
    }
}

// Función para guardar los datos en Firestore
fun guardarDatosEnFirestore(
    userId: String?,
    name: String,
    email: String,
    phone: String,
    message: MutableState<String>
) {
    if (userId == null) {
        message.value = "Error: Usuario no autenticado"
        return
    }

    val db = FirebaseFirestore.getInstance()
    val userData = mapOf(
        "name" to name,
        "email" to email,
        "phone" to phone
    )

    db.collection("users").document(userId).update(userData)
        .addOnSuccessListener {
            message.value = "Datos actualizados exitosamente"
        }
        .addOnFailureListener { e ->
            message.value = "Error al actualizar datos: ${e.message}"
        }
}
