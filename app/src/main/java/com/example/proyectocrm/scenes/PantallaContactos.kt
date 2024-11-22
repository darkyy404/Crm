package com.example.proyectocrm.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaContactos(navHostController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val contactos = listOf(
        Contacto("Vincent Moody", "Último mensaje"),
        Contacto("Bradley Malone", "Visto hace 10 min"),
        Contacto("Janie Todd", "¿Te llegó la info?"),
        // Añade más contactos según sea necesario
    )
    val contactosFiltrados = contactos.filter {
        it.nombre.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp) // Espacio para que no tape la lista
        ) {
            // Título centrado en la parte superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Contactos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Barra de búsqueda estilizada
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar contactos") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFECECEC),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Lista de contactos
            LazyColumn {
                items(contactosFiltrados) { contacto ->
                    ContactoCard(contacto = contacto)
                }
            }
        }

        // Botón flotante en la esquina inferior derecha
        FloatingActionButton(
            onClick = {
                // Acción para agregar nuevo contacto
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF007BFF) // Azul
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar contacto", tint = Color.White)
        }
    }
}

@Composable
fun ContactoCard(contacto: Contacto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navegar o abrir chat */ }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder de espacio en vez de imagen
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray), // Representa un avatar genérico
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contacto.nombre.first().toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contacto.nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = contacto.ultimoMensaje,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Más opciones",
            tint = Color.Gray
        )
    }
}

data class Contacto(val nombre: String, val ultimoMensaje: String)

@Preview(showBackground = true)
@Composable
fun PreviewPantallaContactos() {
    PantallaContactos(navHostController = rememberNavController())
}