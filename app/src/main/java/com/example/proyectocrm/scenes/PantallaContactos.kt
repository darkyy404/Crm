import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectocrm.components.ContactosViewModel
import com.example.proyectocrm.models.Contacto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaContactos(
    navHostController: NavHostController,
    viewModel: ContactosViewModel = ContactosViewModel() // ViewModel para manejar contactos
) {
    var searchQuery by remember { mutableStateOf("") }
    val contactos by viewModel.contactos.collectAsState()

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
            // Título centrado
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

            // Barra de búsqueda
            SearchBar(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    viewModel.buscarContactos(it)
                }
            )

            // Lista de contactos filtrados con navegación al chat
            ContactList(
                contactos = contactos,
                onContactClick = { contacto ->
                    navHostController.navigate("pantallaChat/${contacto.nombre}")
                }
            )
        }

        // Botón flotante para agregar contactos
        FloatingActionButton(
            onClick = {
                // Acción para agregar contacto
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF007BFF)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar contacto", tint = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Buscar contactos") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFECECEC),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ContactList(contactos: List<Contacto>, onContactClick: (Contacto) -> Unit) {
    LazyColumn {
        items(contactos) { contacto ->
            ContactoCard(contacto = contacto, onClick = { onContactClick(contacto) })
        }
    }
}

@Composable
fun ContactoCard(contacto: Contacto, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = contacto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = contacto.ultimoMensaje, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

data class Contacto(val nombre: String, val ultimoMensaje: String)
