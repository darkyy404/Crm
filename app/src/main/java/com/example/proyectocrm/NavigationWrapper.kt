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


@Composable
fun NavigationWrapper (navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = "pantallaLogin") {

        composable ("pantallaLogin") { PantallaLogin(navHostController) }
        composable ("pantallaRegistro") {PantallaRegistro(navHostController)}
        composable ("pantallaRecuperarContrasena") { PantallaRecuperarContrasena(navHostController) }
        composable ("pantallaHome") { PantallaHome(navHostController) }
        composable("pantallaPerfil") {PantallaPerfil(navHostController) }
        composable("pantallaEditarPerfil") { PantallaEditarPerfil(navHostController) }

    }
}


