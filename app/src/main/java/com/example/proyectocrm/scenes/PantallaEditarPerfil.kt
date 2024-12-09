package com.example.proyectocrm.scenes

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.proyectocrm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarPerfil(navHostController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    val storage = FirebaseStorage.getInstance()

    val context = LocalContext.current

    val name = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val phone = remember { mutableStateOf(TextFieldValue("")) }
    val showPassword = remember { mutableStateOf(false) }
    var dialogState by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) } // URI de la imagen de perfil
    var isDropdownExpanded by remember { mutableStateOf(false) }// Estado del menú desplegable

    // Launchers para manejar la galería y la cámara
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), // Contrato para obtener contenido
        onResult = { uri -> profileImageUri = uri } // Actualiza el URI con el resultado seleccionado
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(), // Contrato para tomar una foto
        onResult = { bitmap ->
            // Convierte el Bitmap obtenido en un URI temporal
            if (bitmap != null) {
                val cacheDir = context.cacheDir // Obtiene la carpeta de caché de la aplicación
                val file = File(cacheDir, "profile_image_${System.currentTimeMillis()}.jpg") // Archivo temporal
                file.outputStream().use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) // Comprime la imagen
                }
                profileImageUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider", // Proveedor configurado en el manifest
                    file
                )
            } else {
                // Si falla, muestra un mensaje de error
                dialogState = Pair(false, "No se pudo capturar la imagen.")
            }
        }
    )

    // Carga inicial de datos del usuario desde Firestore
    LaunchedEffect(Unit) {
        currentUser?.let { user ->
            db.collection("users").document(user.uid).get() // Obtiene el documento del usuario
                .addOnSuccessListener { document ->
                    name.value = TextFieldValue(document.getString("name") ?: "") // Nombre
                    email.value = TextFieldValue(document.getString("email") ?: "") // Email
                    phone.value = TextFieldValue(document.getString("phone") ?: "") // Teléfono
                    // Carga la URL de la imagen desde Firestore
                    val imageUrl = document.getString("profileImageUri")
                    profileImageUri = imageUrl?.let { Uri.parse(it) }
                }
                .addOnFailureListener {
                    // Maneja errores al cargar datos
                    dialogState = Pair(false, "Error al cargar los datos: ${it.message}")
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
        // Barra de navegación con botones "Volver" y "Guardar"
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
                        profileImageUri?.let { uri ->
                            subirImagenAFirebase(storage, uri) { imageUrl, errorMessage ->
                                if (imageUrl != null) {
                                    guardarDatosEnFirestore(
                                        currentUser?.uid,
                                        name.value.text,
                                        email.value.text,
                                        password.value.text,
                                        phone.value.text,
                                        Uri.parse(imageUrl)
                                    ) { success ->
                                        dialogState = if (success) {
                                            Pair(true, "Datos actualizados correctamente")
                                        } else {
                                            Pair(false, "Error al actualizar los datos")
                                        }
                                    }
                                } else {
                                    dialogState = Pair(false, errorMessage ?: "Error desconocido al subir imagen")
                                }
                            }
                        } ?: run {
                            guardarDatosEnFirestore(
                                currentUser?.uid,
                                name.value.text,
                                email.value.text,
                                password.value.text,
                                phone.value.text,
                                null
                            ) { success ->
                                dialogState = if (success) {
                                    Pair(true, "Datos actualizados correctamente")
                                } else {
                                    Pair(false, "Error al actualizar los datos")
                                }
                            }
                        }
                    }
                    .size(24.dp),
                tint = Color(0xFF007AFF)
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de perfil con menú desplegable
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (profileImageUri != null) {
                Image(
                    painter = rememberImagePainter(profileImageUri),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_profile_placeholder),
                    contentDescription = "Imagen predeterminada",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = "Editar imagen",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF007AFF))
                    .clickable { checkAndRequestCameraPermission(context) { isDropdownExpanded = true } }
                    .padding(4.dp)
            )

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        isDropdownExpanded = false
                        galleryLauncher.launch("image/*")
                    },
                    text = { Text("Seleccionar desde galería") }
                )
                DropdownMenuItem(
                    onClick = {
                        isDropdownExpanded = false
                        cameraLauncher.launch(null)
                    },
                    text = { Text("Tomar foto con la cámara") }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        RegisterField("Nombre", name.value, onValueChange = { name.value = it })
        Spacer(modifier = Modifier.height(16.dp))
        RegisterField("Correo electrónico", email.value, onValueChange = { email.value = it }, keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(16.dp))
        RegisterField("Contraseña", password.value, onValueChange = { password.value = it }, keyboardType = KeyboardType.Password, isPassword = true)
        Spacer(modifier = Modifier.height(16.dp))
        RegisterField("Número telefónico", phone.value, onValueChange = { phone.value = it }, keyboardType = KeyboardType.Phone)

        Spacer(modifier = Modifier.height(24.dp))

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

// Función para guardar datos en Firestore, incluida la URI
fun guardarDatosEnFirestore(
    userId: String?,
    name: String,
    email: String,
    password: String,
    phone: String,
    profileImageUri: Uri?,
    onResult: (Boolean) -> Unit
) {
    if (userId == null) {
        onResult(false)
        return
    }

    val db = FirebaseFirestore.getInstance()
    val userData = mutableMapOf(
        "name" to name,
        "email" to email,
        "password" to password,
        "phone" to phone
    )

    profileImageUri?.let { uri ->
        userData["profileImageUri"] = uri.toString()
    }

    db.collection("users").document(userId).set(userData)
        .addOnSuccessListener { onResult(true) }
        .addOnFailureListener { onResult(false) }
}

// Subir imagen a Firebase Storage
fun subirImagenAFirebase(storage: FirebaseStorage, uri: Uri, onResult: (String?, String?) -> Unit) {
    val storageRef = storage.reference.child("profile_images/${UUID.randomUUID()}.jpg")

    storageRef.putFile(uri)
        .addOnSuccessListener { taskSnapshot ->
            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                onResult(downloadUri.toString(), null) // Devuelve la URL de descarga
            }.addOnFailureListener { e ->
                onResult(null, e.message)
            }
        }
        .addOnFailureListener { e ->
            onResult(null, e.message)
        }
}


// Verificar y solicitar permisos
fun checkAndRequestCameraPermission(context: Context, onPermissionGranted: () -> Unit) {
    val cameraPermission = Manifest.permission.CAMERA
    if (ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted()
    } else {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(cameraPermission),
            100
        )
    }
}
