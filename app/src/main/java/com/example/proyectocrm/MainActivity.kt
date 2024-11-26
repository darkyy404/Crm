package com.example.proyectocrm

import com.example.proyectocrm.scripts.DataImporter // Asegúrate de que esta importación sea correcta
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navHostController = rememberNavController()
            NavigationWrapper(navHostController)
        }

        // Llama al método para importar datos en Firebase
        importarDatosFirebase()
    }

    /**
     * Función para importar datos en Firebase utilizando el DataImporter.
     */
    private fun importarDatosFirebase() {
        DataImporter.importarDatosAutomatizados()
    }
}
