package com.example.proyectocrm.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.proyectocrm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PantallaPerfil(navHostController: NavHostController) {
    // Estados para almacenar los datos del usuario autenticado
    var userName by remember { mutableStateOf("Nombre del Usuario") }
    var userEmail by remember { mutableStateOf("usuario@email.com") }
    var profileImageUrl by remember { mutableStateOf<String?>(null) } // URL de la imagen de perfil

    // Cargar los datos del usuario autenticado desde Firebase
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            userName = it.displayName ?: "Nombre no disponible"
            userEmail = it.email ?: "Correo no disponible"

            // Lógica para cargar la URL de la imagen desde Firestore
            obtenerImagenDePerfil(it.uid) { url ->
                profileImageUrl = url
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFFEDF1F3), Color(0xFFFFFFFF))))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Imagen del perfil
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (profileImageUrl != null) {
                // Si se cargó una URL de Firebase, mostrar la imagen usando Coil
                Image(
                    painter = rememberImagePainter(profileImageUrl),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Mostrar un placeholder mientras la URL de la imagen se carga
                Image(
                    painter = painterResource(R.drawable.ic_profile_placeholder),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre y correo del usuario
        Text(
            text = userName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F1F1F)
        )
        Text(
            text = userEmail,
            fontSize = 14.sp,
            color = Color(0xFF5A5A5A)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Opciones de perfil
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                OpcionDePerfil(
                    icono = R.drawable.ic_edit,
                    texto = "Edit Profile",
                    onClick = { navHostController.navigate("pantallaEditarPerfil") }
                )
                Divider(color = Color(0xFFEDF1F3), thickness = 1.dp)
                OpcionDePerfil(
                    icono = R.drawable.ic_logout,
                    texto = "Logout",
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navHostController.navigate("pantallaLogin") {
                            popUpTo("pantallaPerfil") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

// Función para cargar la URL de la imagen desde Firestore
fun obtenerImagenDePerfil(userId: String, onResult: (String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(userId).get()
        .addOnSuccessListener { document ->
            val imageUrl = document.getString("profileImageUri") // Campo donde se guarda la URL
            onResult(imageUrl) // Retornar la URL al callback
        }
        .addOnFailureListener {
            onResult(null) // En caso de error, retornar null
        }
}

@Composable
fun OpcionDePerfil(icono: Int, texto: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icono),
            contentDescription = texto,
            tint = Color(0xFF007AFF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F1F1F)
        )
    }
}

