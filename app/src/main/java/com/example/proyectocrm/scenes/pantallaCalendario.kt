import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCalendario(navHostController: NavHostController) {
    // Variables para almacenar la fecha seleccionada, el título del evento y los detalles
    var selectedDate by remember { mutableStateOf("") }
    var evento by remember { mutableStateOf("") }
    var detalles by remember { mutableStateOf("") }
    val eventosMap = remember { mutableStateMapOf<String, Pair<String, String>>() } // Mapa para almacenar eventos y detalles por fecha

    // Inicializar Firestore para interactuar con la base de datos
    val db = FirebaseFirestore.getInstance()

    // Función para cargar eventos desde Firestore y almacenarlos en eventosMap
    fun cargarEventos() {
        db.collection("eventos").get()
            .addOnSuccessListener { documents ->
                eventosMap.clear()
                for (document in documents) {
                    val fecha = document.getString("fecha") ?: ""
                    val titulo = document.getString("evento") ?: ""
                    val detalle = document.getString("detalles") ?: ""
                    if (fecha.isNotEmpty() && titulo.isNotEmpty()) {
                        eventosMap[fecha] = Pair(titulo, detalle)
                    }
                }
            }
            .addOnFailureListener {
                // Manejar errores al cargar eventos
            }
    }

    // Cargar eventos al iniciar el componente
    LaunchedEffect(Unit) {
        cargarEventos()
    }

    // Configurar el selector de fechas (DatePickerDialog)
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Formatear la fecha seleccionada y actualizar selectedDate
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR), // Año actual
        calendar.get(Calendar.MONTH), // Mes actual
        calendar.get(Calendar.DAY_OF_MONTH) // Día actual
    )

    // Estructura principal de la pantalla
    Scaffold(
        // Barra inferior de navegación
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentColor = Color.Gray
            ) {
                // Botón para navegar a la pantalla de inicio
                IconButton(
                    onClick = { navHostController.navigate("pantallaHome") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color(0xFF007AFF))
                }
                // Botón para navegar a la pantalla de contactos
                IconButton(
                    onClick = { navHostController.navigate("pantallaContactos") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Contacts, contentDescription = "Contactos", tint = Color(0xFF007AFF))
                }
                // Botón para navegar a la pantalla de calendario (actual)
                IconButton(
                    onClick = { navHostController.navigate("pantallaCalendario") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Calendario", tint = Color(0xFF007AFF))
                }
                // Botón para navegar al menú
                IconButton(
                    onClick = { navHostController.navigate("pantallaMenu") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color(0xFF007AFF))
                }
            }
        }
    ) { paddingValues ->
        // Contenido principal de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mostrar la fecha seleccionada
            Text(
                text = if (selectedDate.isEmpty()) "No hay fecha seleccionada" else "Fecha seleccionada: $selectedDate",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para abrir el selector de fechas
            Button(
                onClick = { datePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)) // Fondo azul
            ) {
                Text(text = "Seleccionar Fecha", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para ingresar el título del evento
            OutlinedTextField(
                value = evento,
                onValueChange = { evento = it },
                label = { Text("Título del Evento", color = Color(0xFF007AFF)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF007AFF),
                    unfocusedBorderColor = Color(0xFF007AFF),
                    cursorColor = Color(0xFF007AFF)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para ingresar los detalles del evento
            OutlinedTextField(
                value = detalles,
                onValueChange = { detalles = it },
                label = { Text("Detalles del Evento", color = Color(0xFF007AFF)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF007AFF),
                    unfocusedBorderColor = Color(0xFF007AFF),
                    cursorColor = Color(0xFF007AFF)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar el evento
            Button(
                onClick = {
                    if (selectedDate.isNotEmpty() && evento.isNotEmpty()) {
                        val eventoData = mapOf(
                            "fecha" to selectedDate,
                            "evento" to evento,
                            "detalles" to detalles
                        )
                        db.collection("eventos")
                            .add(eventoData)
                            .addOnSuccessListener {
                                eventosMap[selectedDate] = Pair(evento, detalles)
                                evento = "" // Limpiar el campo de evento
                                detalles = "" // Limpiar el campo de detalles
                            }
                            .addOnFailureListener {
                                // Manejar errores al guardar
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)) // Fondo azul
            ) {
                Text(text = "Guardar Evento", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar los eventos guardados
            Text("Eventos Guardados:", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Lista de eventos guardados
                items(eventosMap.entries.toList()) { (fecha, datos) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Fecha: $fecha",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = "Título: ${datos.first}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black
                            )
                            Text(
                                text = "Detalles: ${datos.second}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
