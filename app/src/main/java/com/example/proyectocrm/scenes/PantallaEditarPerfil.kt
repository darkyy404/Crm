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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lint.kotlin.metadata.Visibility
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
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val phone = remember { mutableStateOf(TextFieldValue("")) }
    val showPassword = remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf<Pair<Boolean, Boolean>?>(null) }

    // Efecto para cargar datos de Firestore
    LaunchedEffect(Unit) {
        currentUser?.let { user ->
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    name.value = TextFieldValue(document.getString("name") ?: "")
                    email.value = TextFieldValue(document.getString("email") ?: "")
                    phone.value = TextFieldValue(document.getString("phone") ?: "")
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
                        guardarDatosEnFirestore(
                            currentUser?.uid,
                            name.value.text,
                            email.value.text,
                            password.value.text,
                            phone.value.text
                        ) { success ->
                            showDialog = Pair(success, success)
                        }
                    }
                    .size(24.dp),
                tint = Color(0xFF007AFF)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de perfil editable
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.LightGray, CircleShape),
            contentAlignment = Alignment.BottomEnd
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile_placeholder),
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                tint = Color.White
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Editar imagen",
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFF007AFF), CircleShape)
                    .padding(4.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campos editables
        EditProfileField("Nombre", name.value, onValueChange = { name.value = it })
        EditProfileField("Correo electrónico", email.value, onValueChange = { email.value = it }, keyboardType = KeyboardType.Email)
        EditProfileField(
            "Contraseña",
            password.value,
            onValueChange = { password.value = it },
            keyboardType = KeyboardType.Password,
            isPassword = true,
            showPassword = showPassword
        )
        EditProfileField("Número telefónico", phone.value, onValueChange = { phone.value = it }, keyboardType = KeyboardType.Phone)

        Spacer(modifier = Modifier.height(16.dp))

        // Diálogo para el mensaje
        showDialog?.let { (success, isPositive) ->
            AlertDialog(
                onDismissRequest = { showDialog = null },
                title = { Text(if (success) "¡Éxito!" else "Error") },
                text = { Text(if (success) "Datos actualizados correctamente" else "Hubo un problema al actualizar los datos.") },
                confirmButton = {
                    TextButton(onClick = { showDialog = null }) {
                        Text("OK")
                    }
                },
                icon = {
                    Icon(
                        painterResource(if (isPositive) R.drawable.ic_success else R.drawable.ic_error),
                        contentDescription = null,
                        tint = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                }
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
    isPassword: Boolean = false,
    showPassword: MutableState<Boolean> = mutableStateOf(false)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            placeholder = { Text(label, color = Color.Gray) },
            visualTransformation = if (isPassword && !showPassword.value) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = if (isPassword) {
                {
                    Icon(
                        imageVector = if (showPassword.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar contraseña",
                        modifier = Modifier.clickable { showPassword.value = !showPassword.value }
                    )
                }
            } else null
            ,
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
    password: String,
    phone: String,
    onResult: (Boolean) -> Unit
) {
    if (userId == null) {
        onResult(false)
        return
    }

    val db = FirebaseFirestore.getInstance()
    val userData = mapOf(
        "name" to name,
        "email" to email,
        "password" to password,
        "phone" to phone
    )

    db.collection("users").document(userId).update(userData)
        .addOnSuccessListener { onResult(true) }
        .addOnFailureListener { onResult(false) }
}
