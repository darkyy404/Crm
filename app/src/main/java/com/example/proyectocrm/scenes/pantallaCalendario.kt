import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCalendario(navHostController: NavHostController) {
    // Variable para almacenar la fecha seleccionada
    var selectedDate: String by remember { mutableStateOf("") }

    // Obtener el contexto de Android desde Jetpack Compose
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Configurar el DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Interfaz de usuario
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón para mostrar el DatePickerDialog
            Button(
                onClick = { datePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)) // Fondo azul
            ) {
                Text(text = "Seleccionar Fecha", color = Color.White) // Texto blanco
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar la fecha seleccionada
            Text(
                text = if (selectedDate.isEmpty()) "No hay fecha seleccionada" else "Fecha seleccionada: $selectedDate",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
