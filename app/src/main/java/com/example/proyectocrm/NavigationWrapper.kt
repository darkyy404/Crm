package com.example.proyectocrm

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.proyectocrm.scenes.PantallaEditarPerfil
import com.example.proyectocrm.scenes.PantallaLogin
import com.example.proyectocrm.scenes.PantallaRegistro
import com.example.proyectocrm.scenes.PantallaRecuperarContrasena
import com.example.proyectocrm.scenes.PantallaHome
import com.example.proyectocrm.scenes.PantallaPerfil
import com.example.proyectocrm.scenes.acceso.PantallaAccesoSeguro
import com.example.proyectocrm.scenes.acceso.PantallaAccesoFallido
import com.example.proyectocrm.scenes.acceso.PantallaCambiarMetodoAcceso
import com.example.proyectocrm.scenes.acceso.PantallaConfiguracionAcceso

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
        composable("pantallaConfiguracionAccesoSeguro") { PantallaConfiguracionAcceso(navHostController) }
        composable("pantallaAccesoSeguro") { PantallaAccesoSeguro(navHostController) }
        composable("pantallaAccesoFallido") { PantallaAccesoFallido(navHostController) }
        composable("pantallaCambiarMetodoAcceso") { PantallaCambiarMetodoAcceso(navHostController) }
    }
}
