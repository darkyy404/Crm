package com.example.proyectocrm.scenes

import LineChartComponent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectocrm.R
import com.example.proyectocrm.components.OrderList
import com.github.mikephil.charting.data.Entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(navHostController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }

    // Datos para los gráficos
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
        // Título y perfil
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
            Icon(
                painter = painterResource(id = R.drawable.ic_profile_placeholder),
                contentDescription = "Perfil",
                tint = Color(0xFF007AFF),
                modifier = Modifier.size(32.dp)
            )
        }

        // Tabs
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

        // Gráfico
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

        // Balance total
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

        // Últimos pedidos
        Text(
            text = "Últimos Pedidos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Componente de lista de pedidos
        OrderList()
    }
}
