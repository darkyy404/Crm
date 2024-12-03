package com.example.proyectocrm

import Contacto
import PantallaContactos
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectocrm.scenes.PantallaChat
import com.example.proyectocrm.scenes.PantallaEditarPerfil
import com.example.proyectocrm.scenes.PantallaLogin
import com.example.proyectocrm.scenes.PantallaRegistro
import com.example.proyectocrm.scenes.PantallaRecuperarContrasena
import com.example.proyectocrm.scenes.PantallaHome
import com.example.proyectocrm.scenes.PantallaPerfil
import com.example.proyectocrm.scenes.acceso.PantallaAccesoSeguro
import com.example.proyectocrm.scenes.acceso.PantallaAccesoFallido
import com.example.proyectocrm.scenes.acceso.PantallaConfiguracionAcceso
import com.example.proyectocrm.scenes.acceso.PantallaConfigurarPin

@Composable
fun NavigationWrapper(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = "pantallaLogin") {

        // Pantallas principales
        composable("pantallaLogin") { PantallaLogin(navHostController) }
        composable("pantallaRegistro") { PantallaRegistro(navHostController) }
        composable("pantallaRecuperarContrasena") { PantallaRecuperarContrasena(navHostController) }
        composable("pantallaHome") { PantallaHome(navHostController) }
        composable("pantallaPerfil") { PantallaPerfil(navHostController) }
        composable("pantallaEditarPerfil") { PantallaEditarPerfil(navHostController) }

        // Pantallas de acceso seguro
        composable("pantallaConfiguracionAcceso") { PantallaConfiguracionAcceso(navHostController) }
        composable("pantallaAccesoSeguro") { PantallaAccesoSeguro(navHostController) }
        composable("pantallaAccesoFallido") { PantallaAccesoFallido(navHostController) }

        // Pantalla de configuración de PIN
        composable("pantallaConfigurarPin") { PantallaConfigurarPin(navHostController) }

        // Pantalla de contactos
        composable("pantallaContactos") { PantallaContactos(navHostController) }
        composable(
            "pantallaChat/{nombreContacto}",
            arguments = listOf(navArgument("nombreContacto") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombreContacto = backStackEntry.arguments?.getString("nombreContacto") ?: ""
            PantallaChat(navHostController, Contacto(nombre = nombreContacto, ultimoMensaje = ""))
        }

    }
}
