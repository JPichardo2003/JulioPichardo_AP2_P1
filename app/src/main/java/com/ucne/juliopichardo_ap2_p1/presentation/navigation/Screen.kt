package com.ucne.juliopichardo_ap2_p1.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object List : Screen()
    @Serializable
    data class Registro(val tecnicoId: Int) : Screen()
}
@Composable
fun Parcial1NavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.List
    ) {
        composable<Screen.List> {
            //Aqui va la lista
        }
        composable<Screen.Registro> {
            //Aqui va el registro
        }
    }
}