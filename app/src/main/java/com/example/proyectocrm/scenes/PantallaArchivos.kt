package com.example.proyectocrm.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectocrm.R

// Modelo de datos para representar archivos
data class Archivo(val nombre: String, val tipo: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaArchivos(
    navHostController: NavHostController,
    archivos: List<Archivo>
) {
    // Determina qué pantalla mostrar basado en si existen archivos
    if (archivos.isEmpty()) {
        PantallaSinArchivos(navHostController)
    } else {
        PantallaConArchivos(navHostController, archivos)
    }
}

// Pantalla para el caso donde NO hay archivos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaSinArchivos(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Archivos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Acción para subir un archivo */ },
                containerColor = Color(0xFF007AFF)
            ) {
                Text("Upload", color = Color.White)
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentColor = Color.Gray
            ) {
                IconButton(
                    onClick = { navHostController.navigate("home") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Inicio", tint = Color(0xFF007AFF))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_files), // Reemplaza con tu recurso de imagen
                contentDescription = "No hay archivos",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No files and folders found",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Try to upload more files to your storage or create a new folder from your desktop.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

// Pantalla para el caso donde HAY archivos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConArchivos(
    navHostController: NavHostController,
    archivos: List<Archivo>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Archivos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                ),
                actions = {
                    IconButton(onClick = { /* Implementar búsqueda o acciones extra */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones", tint = Color.Black)
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentColor = Color.Gray
            ) {
                IconButton(
                    onClick = { navHostController.navigate("home") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Inicio", tint = Color(0xFF007AFF))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mis Archivos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Archivos que pertenecen a ti",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(archivos) { archivo ->
                    ArchivoItem(archivo)
                }
            }
        }
    }
}

// Componente para cada ítem de archivo
@Composable
fun ArchivoItem(archivo: Archivo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Icono del archivo según su tipo
            val icono = when (archivo.tipo) {
                "pdf" -> R.drawable.ic_pdf // Reemplaza con tu recurso de ícono PDF
                "docx" -> R.drawable.ic_word // Reemplaza con tu recurso de ícono Word
                "pptx" -> R.drawable.ic_ppt // Reemplaza con tu recurso de ícono PPT
                else -> R.drawable.ic_folder // Reemplaza con un ícono genérico
            }

            Image(
                painter = painterResource(id = icono),
                contentDescription = "Icono de archivo",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Nombre del archivo
            Text(
                text = archivo.nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            // Botón de más opciones
            IconButton(onClick = { /* Implementar menú de opciones */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.Gray)
            }
        }
    }
}
