import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectocrm.models.Contacto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaContactos(
    navHostController: NavHostController,
    viewModel: ContactosViewModel
) {
    val contactos by viewModel.contactos.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredContactos = contactos.filter {
        it.nombre.contains(searchQuery, ignoreCase = true) || it.email.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Contactos", color = Color(0xFF1F1F1F)) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigate("pantallaHome") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color(0xFF007AFF))
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navHostController.navigate("pantallaCrearContacto") },
                containerColor = Color(0xFF007AFF)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar contacto", tint = Color.White)
            }
        },
        bottomBar = {
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
                    onClick = { navHostController.navigate("pantallaMenu") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color(0xFF007AFF))
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

            // Barra de búsqueda funcional
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar contacto", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Gray)
                },
                trailingIcon = {
                    Icon(Icons.Default.FilterList, contentDescription = "Filtrar", tint = Color.Gray)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de contactos filtrados
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredContactos) { contacto ->
                    ContactoCard(
                        contacto = contacto,
                        viewModel = viewModel,
                        navHostController = navHostController,
                        onClick = {
                            navHostController.navigate(
                                "pantallaChat/${Uri.encode(contacto.nombre)}"
                            )
                        }
                    )
                }
            }


        }
    }
}




@Composable
fun ContactoCard(
    contacto: Contacto,
    viewModel: ContactosViewModel,
    navHostController: NavHostController,
    onClick: () -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Acción al hacer clic en la tarjeta (puede navegar al detalle del contacto)
                navHostController.navigate("pantallaChat/${Uri.encode(contacto.nombre)}")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de perfil (placeholder)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFF5F5F5), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contacto.nombre.firstOrNull()?.toString() ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF007AFF)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información del contacto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contacto.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1F1F)
                )
                Text(
                    text = contacto.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Menú de opciones
            Box {
                IconButton(onClick = { expandedMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.Gray)
                }
                DropdownMenu(
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false }
                ) {
                    // Opción para Editar
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            expandedMenu = false
                            viewModel.seleccionarContacto(contacto) // Selecciona el contacto en el ViewModel
                            navHostController.navigate("pantallaEditarContacto") // Navega a la pantalla de edición
                        }
                    )
                    // Opción para Eliminar
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expandedMenu = false
                            viewModel.eliminarContacto(contacto) // Llama al método de eliminar en el ViewModel
                        }
                    )
                }
            }
        }
    }
}


