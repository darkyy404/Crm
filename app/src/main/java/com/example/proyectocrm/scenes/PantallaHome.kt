package com.example.proyectocrm.scenes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectocrm.scenes.home.LineChartComponent
import com.github.mikephil.charting.data.Entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(navHostController: NavHostController) {
    // Estado para rastrear la pestaña seleccionada
    var selectedTab by remember { mutableStateOf(0) }

    // Datos de ejemplo para los gráficos
    val leadsData = listOf(
        Entry(1f, 5000f),
        Entry(2f, 7000f),
        Entry(3f, 9000f),
        Entry(4f, 8500f)
    )
    val salesData = listOf(
        Entry(1f, 1.5f),
        Entry(2f, 2.3f),
        Entry(3f, 1.8f),
        Entry(4f, 3.0f)
    )
    val ordersData = listOf(
        Entry(1f, 200f),
        Entry(2f, 400f),
        Entry(3f, 600f),
        Entry(4f, 800f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título principal
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pestañas
        TabRow(selectedTabIndex = selectedTab, modifier = Modifier.fillMaxWidth()) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Leads") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Ventas") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Pedidos") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenedor de gráfico con ChartComponent
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

        // Información de balance total
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Balance Total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "782,123.56€",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "+1.7% este mes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Últimos pedidos (placeholder)
        Text(
            text = "Últimos Pedidos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Placeholder para la lista de pedidos
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Pedidos Placeholder", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
