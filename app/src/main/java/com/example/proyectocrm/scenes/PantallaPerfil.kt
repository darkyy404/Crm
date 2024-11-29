package com.example.proyectocrm.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectocrm.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaPerfil(navHostController: NavHostController) {
    // Estados para almacenar los datos del usuario autenticado
    var userName by remember { mutableStateOf("Nombre del Usuario") }
    var userEmail by remember { mutableStateOf("usuario@email.com") }

    // Cargar los datos del usuario autenticado desde Firebase
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser // Usuario actual
        currentUser?.let {
            userName = it.displayName ?: "Nombre no disponible" // Si no hay nombre, mostrar mensaje predeterminado
            userEmail = it.email ?: "Correo no disponible" // Si no hay correo, mostrar mensaje predeterminado
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen del perfil
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_profile_placeholder), // Reemplazar con la imagen del perfil
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar nombre y correo del usuario
        Text(
            text = userName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = userEmail,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Opciones disponibles (Edit Profile y Logout)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            OpcionDePerfil(
                icono = R.drawable.ic_edit,
                texto = "Edit Profile",
                onClick = { navHostController.navigate("pantallaEditarPerfil") }
            )

            OpcionDePerfil(
                icono = R.drawable.ic_logout,
                texto = "Logout",
                onClick = { /* Lógica para cerrar sesión */ }
            )
        }
    }
}



@Composable
fun OpcionDePerfil(icono: Int, texto: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
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
            fontWeight = FontWeight.Medium
        )
    }
}
