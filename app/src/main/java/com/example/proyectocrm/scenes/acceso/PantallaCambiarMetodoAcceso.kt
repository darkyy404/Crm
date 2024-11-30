package com.example.proyectocrm.scenes.acceso

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun PantallaCambiarMetodoAcceso(navHostController: NavHostController) {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf(leerPreferencia(context, "auth_method")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cambiar método de acceso", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        RadioButtonWithLabel(
            label = "Usar mi huella dactilar",
            isSelected = selectedOption == "fingerprint",
            onClick = { selectedOption = "fingerprint" }
        )

        Spacer(modifier = Modifier.height(8.dp))

        RadioButtonWithLabel(
            label = "Usar un PIN",
            isSelected = selectedOption == "pin",
            onClick = { selectedOption = "pin" }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            guardarPreferencia(context, "auth_method", selectedOption ?: "")
            navHostController.navigate("pantallaPrincipal")
        }) {
            Text("Guardar Cambios")
        }
    }
}

