import LineChartComponent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.proyectocrm.R
import com.example.proyectocrm.components.ChartRepository
import com.example.proyectocrm.components.OrderList
import com.example.proyectocrm.scenes.obtenerImagenDePerfil
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(navHostController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }

    var leadsData by remember { mutableStateOf<List<Entry>>(emptyList()) }
    var salesData by remember { mutableStateOf<List<Entry>>(emptyList()) }
    var ordersData by remember { mutableStateOf<List<Entry>>(emptyList()) }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            leadsData = ChartRepository.getChartData("leads").distinctBy { it.x }
            salesData = ChartRepository.getChartData("sales").distinctBy { it.x }
            ordersData = ChartRepository.getChartData("orders_chart").distinctBy { it.x }
        }
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            obtenerImagenDePerfil(it.uid) { url ->
                profileImageUrl = url
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado con el título del dashboard y el ícono de perfil
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { navHostController.navigate("pantallaPerfil") }) {
                    if (profileImageUrl != null) {
                        Image(
                            painter = rememberImagePainter(profileImageUrl),
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pestañas para seleccionar diferentes gráficos
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF007AFF)
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Leads") },
                    selectedContentColor = Color(0xFF007AFF),
                    unselectedContentColor = Color.Gray
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Ventas") },
                    selectedContentColor = Color(0xFF007AFF),
                    unselectedContentColor = Color.Gray
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Pedidos") },
                    selectedContentColor = Color(0xFF007AFF),
                    unselectedContentColor = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Contenedor del gráfico
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (selectedTab) {
                    0 -> LineChartComponent(dataPoints = leadsData, label = "Leads")
                    1 -> LineChartComponent(dataPoints = salesData, label = "Ventas")
                    2 -> LineChartComponent(dataPoints = ordersData, label = "Pedidos")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta para mostrar el balance total
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3366FF))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Balance Total",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "782,123.56€",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "+1.7% este mes",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFEDF1F3)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título de la lista de pedidos
            Text(
                text = "Últimos Pedidos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Componente para la lista de pedidos
            OrderList()
        }

        // Botón flotante con icono de contacto
        FloatingActionButton(
            onClick = { navHostController.navigate("pantallaContactos") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF007AFF)
        ) {
            Icon(Icons.Default.Contacts, contentDescription = "Ir a Contactos", tint = Color.White)
        }
    }
}
