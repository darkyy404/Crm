package com.example.proyectocrm.scenes.acceso

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectocrm.components.guardarPreferencia

@Composable
fun PantallaConfiguracionAcceso(navHostController: NavHostController) {
    val context = LocalContext.current
    val selectedOption = "pin" // Método fijo a "pin" como único método de acceso seguro

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Configurar Acceso Seguro",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Configura tu método de acceso seguro:")

        Spacer(modifier = Modifier.height(16.dp))

        // Opción fija para el PIN
        RadioButtonWithLabel(
            label = "Usar un PIN",
            isSelected = true, // Siempre seleccionado porque es la única opción
            onClick = {} // Sin acción porque no hay otras opciones
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            guardarPreferencia(context, "auth_method", selectedOption) // Guarda el método de acceso como "pin"
            navHostController.navigate("pantallaConfigurarPin") // Navega para configurar el PIN
        }) {
            Text("Guardar y Configurar PIN")
        }
    }
}

// Componente reutilizable para botones de selección
@Composable
fun RadioButtonWithLabel(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = isSelected, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}
