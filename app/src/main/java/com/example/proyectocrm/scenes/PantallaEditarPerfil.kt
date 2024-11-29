package com.example.proyectocrm.scenes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.proyectocrm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarPerfil(navHostController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    val storage = FirebaseStorage.getInstance()

    // Estados para los datos del usuario
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val phone = remember { mutableStateOf(TextFieldValue("")) }
    val showPassword = remember { mutableStateOf(false) }
    var dialogState by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Lógica para cargar datos del usuario desde Firestore
    LaunchedEffect(Unit) {
        currentUser?.let { user ->
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    name.value = TextFieldValue(document.getString("name") ?: "")
                    email.value = TextFieldValue(document.getString("email") ?: "")
                    phone.value = TextFieldValue(document.getString("phone") ?: "")
                }
                .addOnFailureListener {
                    dialogState = Pair(false, "Error al cargar los datos: ${it.message}")
                }
        }
    }

    // Launchers para galería y cámara
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> profileImageUri = uri }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            // Lógica para convertir Bitmap a URI si es necesario
            profileImageUri = Uri.parse("path/to/generated/bitmap") // Implementa la lógica de guardado
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F3))
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Barra de navegación
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
                            dialogState = if (success) {
                                Pair(true, "Datos actualizados correctamente")
                            } else {
                                Pair(false, "Error al actualizar los datos")
                            }
                        }

                        profileImageUri?.let { uri ->
                            subirImagenAFirebase(storage, uri) { successMessage, errorMessage ->
                                dialogState = if (successMessage != null) {
                                    Pair(true, successMessage)
                                } else {
                                    Pair(false, errorMessage ?: "Error desconocido al subir imagen")
                                }
                            }
                        }
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
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (profileImageUri != null) {
                Image(
                    painter = rememberImagePainter(profileImageUri),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile_placeholder),
                    contentDescription = "Imagen predeterminada",
                    modifier = Modifier.size(100.dp),
                    tint = Color.White
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Seleccionar imagen",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
                    .clickable { galleryLauncher.launch("image/*") },
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campos editables
        RegisterField("Nombre", name.value, onValueChange = { name.value = it })
        RegisterField("Correo electrónico", email.value, onValueChange = { email.value = it }, keyboardType = KeyboardType.Email)
        RegisterField("Contraseña", password.value, onValueChange = { password.value = it }, keyboardType = KeyboardType.Password, isPassword = true)
        RegisterField("Número telefónico", phone.value, onValueChange = { phone.value = it }, keyboardType = KeyboardType.Phone)

        Spacer(modifier = Modifier.height(16.dp))

        // Diálogo de éxito/error
        dialogState?.let { (success, message) ->
            AlertDialog(
                onDismissRequest = { dialogState = null },
                title = { Text(if (success) "¡Éxito!" else "Error") },
                text = { Text(message) },
                confirmButton = {
                    TextButton(onClick = { dialogState = null }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

// Función para guardar datos
fun guardarDatosEnFirestore(userId: String?, name: String, email: String, password: String, phone: String, onResult: (Boolean) -> Unit) {
    if (userId == null) {
        onResult(false)
        return
    }

    val db = FirebaseFirestore.getInstance()
    val userData = mapOf("name" to name, "email" to email, "password" to password, "phone" to phone)

    db.collection("users").document(userId).update(userData)
        .addOnSuccessListener { onResult(true) }
        .addOnFailureListener { onResult(false) }
}

// Subir imagen a Firebase Storage
fun subirImagenAFirebase(storage: FirebaseStorage, uri: Uri, onResult: (String?, String?) -> Unit) {
    val storageRef = storage.reference.child("profile_images/${UUID.randomUUID()}.jpg")

    storageRef.putFile(uri)
        .addOnSuccessListener { onResult("Imagen subida exitosamente", null) }
        .addOnFailureListener { e -> onResult(null, e.message) }
}
