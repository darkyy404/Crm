
package com.example.proyectocrm.scenes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectocrm.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


// Modelo de datos para representar archivos
data class Archivo(val nombre: String, val tipo: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaArchivos(
    navHostController: NavHostController,
    archivos: List<Archivo>, // Lista de archivos
    onArchivoClick: (Archivo) -> Unit // Acción al hacer clic en un archivo
) {
    // Scope para operaciones asíncronas
    val scope = rememberCoroutineScope()

    // Referencia al almacenamiento de Firebase
    val storageRef = FirebaseStorage.getInstance().reference

    // URI del archivo seleccionado
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    // Lista de archivos en estado mutable para actualizar la UI
    var archivosState by remember { mutableStateOf(archivos) }

    // Configuración del lanzador para seleccionar archivos
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedFileUri = uri // Guardamos el archivo seleccionado
        uri?.let {
            scope.launch(Dispatchers.IO) {
                // Subir archivo a Firebase y actualizar la lista
                val nuevoArchivo = subirArchivoAFirebase(it, storageRef)
                nuevoArchivo?.let { archivo ->
                    archivosState = archivosState + archivo // Actualizar la lista con el nuevo archivo
                }
            }
        }
    }

    // Mostrar pantalla según si hay archivos o no
    if (archivosState.isEmpty()) {
        PantallaSinArchivos(navHostController) {
            filePickerLauncher.launch("*/*") // Abrir el selector de archivos
        }
    } else {
        PantallaConArchivos(navHostController, archivosState, onArchivoClick) {
            filePickerLauncher.launch("*/*") // Abrir el selector de archivos
        }
    }
}
// Pantalla para el caso donde NO hay archivos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaSinArchivos(
    navHostController: NavHostController,
    onUploadClick: () -> Unit
) {
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
                onClick = onUploadClick, // Acción para seleccionar archivo
                containerColor = Color(0xFF007AFF)
            ) {
                Text("Subir", color = Color.White)
            }
        },
        bottomBar = {
            BottomNavigationBar(navHostController)
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
                painter = painterResource(id = R.drawable.ic_no_files),
                contentDescription = "No hay archivos",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No hay archivos disponibles",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Pantalla para el caso donde HAY archivos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConArchivos(
    navHostController: NavHostController,
    archivos: List<Archivo>,
    onArchivoClick: (Archivo) -> Unit,
    onUploadClick: () -> Unit
) {
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
                onClick = onUploadClick, // Acción para seleccionar archivo
                containerColor = Color(0xFF007AFF)
            ) {
                Text("Subir", color = Color.White)
            }
        },
        bottomBar = {
            BottomNavigationBar(navHostController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(archivos) { archivo ->
                ArchivoItem(archivo = archivo) {
                    onArchivoClick(archivo)
                }
            }
        }
    }
}

// Componente para cada ítem de archivo
@Composable
fun ArchivoItem(archivo: Archivo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val icono = when (archivo.tipo) {
                "pdf" -> R.drawable.ic_pdf
                "docx" -> R.drawable.ic_word
                else -> R.drawable.ic_folder
            }
            Image(
                painter = painterResource(id = icono),
                contentDescription = "Archivo",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = archivo.nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Componente de barra de navegación inferior
@Composable
fun BottomNavigationBar(navHostController: NavHostController) {
    BottomAppBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        IconButton(
            onClick = { navHostController.navigate("pantallaHome") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color(0xFF007AFF))
        }
        IconButton(
            onClick = { navHostController.navigate("pantallaContactos") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Contacts, contentDescription = "Contactos", tint = Color(0xFF007AFF))
        }
        IconButton(
            onClick = { navHostController.navigate("pantallaCalendario") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = "Calendario", tint = Color(0xFF007AFF))
        }
        IconButton(
            onClick = { navHostController.navigate("pantallaArchivos") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Archivos", tint = Color(0xFF007AFF))
        }
    }
}

// Función suspendida para subir un archivo a Firebase Storage
suspend fun subirArchivoAFirebase(uri: Uri, storageRef: StorageReference): Archivo? {
    return try {
        val fileName = UUID.randomUUID().toString() // Generar un nombre único
        val uploadTask = storageRef.child("uploads/$fileName").putFile(uri).await()
        val downloadUrl = storageRef.child("uploads/$fileName").downloadUrl.await()

        // Crear un objeto Archivo con el nombre y tipo genérico
        Archivo(nombre = fileName, tipo = "generic") // Cambia "generic" según tu lógica
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

