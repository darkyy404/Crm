package com.example.proyectocrm.scenes

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.proyectocrm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID // Biblioteca para generar nombres únicos para archivos

/**
 * Pantalla de edición de perfil.
 * Permite al usuario actualizar su imagen de perfil seleccionándola desde la galería,
 * capturándola con la cámara y subiéndola al almacenamiento de Firebase.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarPerfil(navHostController: NavHostController) {
    // Obtenemos las instancias de FirebaseAuth y FirebaseStorage
    val currentUser = FirebaseAuth.getInstance().currentUser // Usuario autenticado actual
    val storage = FirebaseStorage.getInstance() // Referencia al almacenamiento de Firebase

    // Estados para manejar la imagen seleccionada o capturada
    var profileImage by remember { mutableStateOf<Uri?>(null) } // URI de la imagen seleccionada
    var bitmap by remember { mutableStateOf<Bitmap?>(null) } // Bitmap para la imagen capturada desde la cámara
    var message by remember { mutableStateOf("") } // Mensaje para mostrar el estado de las operaciones

    // Launcher para seleccionar una imagen desde la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), // Contrato para seleccionar contenido genérico
        onResult = { uri ->
            profileImage = uri // Guardamos la URI seleccionada
            message = "Imagen seleccionada exitosamente" // Notificamos al usuario
        }
    )

    // Launcher para capturar una imagen con la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(), // Contrato para capturar imágenes
        onResult = { capturedBitmap ->
            bitmap = capturedBitmap // Guardamos el bitmap capturado
            message = if (bitmap != null) "Imagen capturada exitosamente" else "No se pudo capturar la imagen"
        }
    )

    // Contenedor principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
            .background(Color(0xFFEDF1F3)) // Fondo claro
            .padding(16.dp), // Margen interno
        horizontalAlignment = Alignment.CenterHorizontally // Alineación central horizontal
    ) {
        // Barra superior con título y botón de guardar
        Row(
            modifier = Modifier
                .fillMaxWidth() // Ocupa todo el ancho
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically // Elementos alineados verticalmente
        ) {
            Text(text = "Editar Perfil", fontSize = 20.sp, modifier = Modifier.weight(1f)) // Título
            Button(
                onClick = {
                    // Intento de subida de imagen al almacenamiento de Firebase
                    profileImage?.let {
                        uploadImageToFirebase(it) { successMessage, errorMessage ->
                            message = successMessage ?: errorMessage ?: "" // Mostrar mensaje según el resultado
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)) // Color del botón
            ) {
                Text("Guardar", color = Color.White) // Etiqueta del botón
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espaciador para separación

        // Imagen de perfil con opciones para editar
        Box(
            modifier = Modifier
                .size(120.dp) // Tamaño del contenedor de la imagen
                .background(Color.Gray, CircleShape), // Fondo gris con forma circular
            contentAlignment = Alignment.BottomEnd // Icono de edición en la esquina inferior derecha
        ) {
            when {
                bitmap != null -> {
                    // Mostrar el bitmap capturado
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier.size(120.dp).clip(CircleShape), // Imagen circular
                        contentScale = ContentScale.Crop // Ajuste de imagen
                    )
                }
                profileImage != null -> {
                    // Mostrar la imagen seleccionada desde la galería
                    Image(
                        painter = rememberImagePainter(profileImage), // Usamos Coil para cargar la imagen
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier.size(120.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    // Mostrar icono predeterminado si no hay imagen
                    Icon(
                        painter = painterResource(R.drawable.ic_profile_placeholder), // Recurso del icono
                        contentDescription = "Icono predeterminado",
                        modifier = Modifier.size(120.dp).clip(CircleShape),
                        tint = Color.White // Color del icono
                    )
                }
            }

            // Icono para abrir opciones de edición
            Icon(
                painter = painterResource(R.drawable.ic_camera), // Icono de cámara
                contentDescription = "Seleccionar Imagen",
                modifier = Modifier
                    .size(32.dp) // Tamaño del icono
                    .background(Color(0xFF007AFF), CircleShape) // Fondo azul
                    .padding(8.dp)
                    .clickable {
                        galleryLauncher.launch("image/*") // Abrir galería al hacer clic
                    },
                tint = Color.White // Color del icono
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones para elegir entre galería o cámara
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Distribución uniforme
        ) {
            Button(
                onClick = { galleryLauncher.launch("image/*") }, // Acción de abrir galería
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
            ) {
                Text("Galería", color = Color.White)
            }
            Button(
                onClick = { cameraLauncher.launch(null) }, // Acción de abrir cámara
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
            ) {
                Text("Cámara", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar mensajes de estado
        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = if (message.startsWith("Error")) Color.Red else Color.Green, // Color según éxito o error
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/**
 * Función para subir imágenes al almacenamiento de Firebase.
 *
 * @param uri La URI de la imagen seleccionada.
 * @param onResult Callback para manejar el éxito o error de la operación.
 */
fun uploadImageToFirebase(uri: Uri, onResult: (String?, String?) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference // Referencia al almacenamiento de Firebase
    val fileName = "profile_images/${UUID.randomUUID()}.jpg" // Nombre único basado en UUID
    val profileRef = storageRef.child(fileName) // Ruta del archivo en el almacenamiento

    // Subir archivo a Firebase Storage
    profileRef.putFile(uri)
        .addOnSuccessListener {
            // Obtener la URL de descarga tras subir exitosamente
            profileRef.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                onResult("Imagen subida exitosamente: $downloadUrl", null) // Llamar al callback con éxito
            }
        }
        .addOnFailureListener { e ->
            onResult(null, "Error al subir la imagen: ${e.message}") // Llamar al callback con error
        }
}
