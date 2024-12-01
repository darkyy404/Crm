package com.example.proyectocrm.scenes.acceso

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@Composable
fun PantallaConfiguracionAcceso(navHostController: NavHostController) {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("pin") } // PIN seleccionado por defecto

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
            isSelected = true, // Siempre seleccionado porque es el único método
            onClick = {} // Sin acción, ya que es la única opción
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

// Guardar datos de configuración en almacenamiento cifrado
fun guardarPreferencia(context: Context, key: String, value: String) {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_preferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    sharedPreferences.edit().putString(key, value).apply()
}
