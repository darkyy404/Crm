package com.example.proyectocrm

import ContactosViewModel
import PantallaCalendario
import PantallaContactos
import PantallaHome
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectocrm.models.Contacto
import com.example.proyectocrm.scenes.PantallaArchivos
import com.example.proyectocrm.scenes.PantallaChat
import com.example.proyectocrm.scenes.PantallaCrearContacto
import com.example.proyectocrm.scenes.PantallaEditarContacto
import com.example.proyectocrm.scenes.PantallaEditarPerfil
import com.example.proyectocrm.scenes.PantallaLogin
import com.example.proyectocrm.scenes.PantallaRegistro
import com.example.proyectocrm.scenes.PantallaRecuperarContrasena
import com.example.proyectocrm.scenes.PantallaPerfil
import com.example.proyectocrm.scenes.acceso.PantallaAccesoSeguro
import com.example.proyectocrm.scenes.acceso.PantallaAccesoFallido
import com.example.proyectocrm.scenes.acceso.PantallaConfiguracionAcceso
import com.example.proyectocrm.scenes.acceso.PantallaConfigurarPin


@Composable
fun NavigationWrapper(navHostController: NavHostController, viewModel: ContactosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    NavHost(navController = navHostController, startDestination = "pantallaLogin") {

        // Pantallas principales
        composable("pantallaLogin") { PantallaLogin(navHostController) }
        composable("pantallaRegistro") { PantallaRegistro(navHostController) }
        composable("pantallaRecuperarContrasena") { PantallaRecuperarContrasena(navHostController) }
        composable("pantallaHome") { PantallaHome(navHostController) }
        composable("pantallaPerfil") { PantallaPerfil(navHostController) }
        composable("pantallaEditarPerfil") { PantallaEditarPerfil(navHostController) }
        composable("pantallaCalendario") { PantallaCalendario(navHostController) }

        // Pantallas de acceso seguro
        composable("pantallaConfiguracionAcceso") { PantallaConfiguracionAcceso(navHostController) }
        composable("pantallaAccesoSeguro") { PantallaAccesoSeguro(navHostController) }
        composable("pantallaAccesoFallido") { PantallaAccesoFallido(navHostController) }

        // Pantalla de configuración de PIN
        composable("pantallaConfigurarPin") { PantallaConfigurarPin(navHostController) }

        // Pantalla de contactos
        composable("pantallaContactos") {
            PantallaContactos(navHostController = navHostController, viewModel )
        }

        // pantalla Chat
        composable(route = "pantallaChat") {
            PantallaChat(navHostController = navHostController, viewModel)
        }

        // Pantalla para crear un contacto
        composable("pantallaCrearContacto") {
            PantallaCrearContacto(
                navHostController = navHostController,
                viewModel = viewModel // Pasamos el mismo ViewModel para compartir la lista de contactos
            )
        }
        // pantalla Editar Contacto
        composable(route = "pantallaEditarContacto") {
            PantallaEditarContacto(navHostController, viewModel)
        }

        // Pantalla Archivos
        composable(route = "pantallaArchivos") {
            PantallaArchivos(
                navHostController = navHostController,
                archivos = emptyList(), // Lista vacía para iniciar sin datos
                onArchivoClick = {} // Función vacía para manejar clics en los archivos
            )
        }



    }
}
