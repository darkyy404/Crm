package com.example.proyectocrm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.proyectocrm.models.Order
import kotlinx.coroutines.tasks.await

@Composable
fun OrderList() {
    // Estado para almacenar los pedidos
    val orders = remember { mutableStateListOf<Order>() }

    // Recuperar los pedidos al iniciar el componente
    LaunchedEffect(Unit) {
        val fetchedOrders = fetchOrders()
        orders.addAll(fetchedOrders)
    }

    // Mostrar la lista de pedidos con LazyColumn
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(orders) { order ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nombre y ID del producto
                Column {
                    Text(
                        text = order.productName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "ID: ${order.orderId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                // Precio del producto
                Text(
                    text = "${order.price}€",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF007AFF)
                )
            }
        }
    }
}

// Función más sencilla para recuperar pedidos desde Firestore
suspend fun fetchOrders(): List<Order> {
    val db = FirebaseFirestore.getInstance()
    return try {
        // Obtener los documentos directamente como objetos Order
        db.collection("orders").get().await().toObjects(Order::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList() // Si hay un error, retornar una lista vacía
    }
}
