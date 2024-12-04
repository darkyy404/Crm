import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
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
    var searchQuery by remember { mutableStateOf("") }
    val contactos by viewModel.contactos.collectAsState()

    Scaffold(
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
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Contactos",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(contactos) { contacto ->
                        ContactoCard(
                            contacto = contacto,
                            onClick = { navHostController.navigate("pantallaChat/${contacto.nombre}") }
                        )
                    }
                }
            }

            // Botón flotante para agregar contactos
            FloatingActionButton(
                onClick = { navHostController.navigate("pantallaCrearContacto") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Color(0xFF007AFF) // Color azul específico
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar contacto",
                    tint = Color.White // Icono en blanco
                )
            }
        }
    }
}

@Composable
fun ContactoCard(contacto: Contacto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = contacto.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = contacto.ultimoMensaje,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
