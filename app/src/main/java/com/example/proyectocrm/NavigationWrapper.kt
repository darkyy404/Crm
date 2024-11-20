package com.example.proyectocrm

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.proyectocrm.scenes.PantallaLogin
import com.example.proyectocrm.scenes.PantallaRegistro




@Composable
fun NavigationWrapper (navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = "pantallaLogin") {

        composable ("pantallaLogin") { PantallaLogin(navHostController) }
        composable ("pantallaRegistro") {PantallaRegistro(navHostController)}
    }
}
