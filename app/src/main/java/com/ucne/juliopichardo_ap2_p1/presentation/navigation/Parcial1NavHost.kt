package com.ucne.juliopichardo_ap2_p1.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ucne.juliopichardo_ap2_p1.presentation.articulo.ArticuloListScreen
import com.ucne.juliopichardo_ap2_p1.presentation.articulo.ArticuloScreen
import com.ucne.juliopichardo_ap2_p1.presentation.servicio.ServicioListScreen
import com.ucne.juliopichardo_ap2_p1.presentation.servicio.ServicioScreen

@Composable
fun Parcial1NavHost(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.ArticuloList
    ) {
        composable<Screen.ServicioList> {
            ServicioListScreen(
                onVerServicio = {
                    navHostController.navigate(Screen.Servicio(it.servicioId ?: 0))
                },
                onAddServicio = {
                    navHostController.navigate(Screen.Servicio(0))
                },
                goToArticlesList = {
                    navHostController.navigate(Screen.ArticuloList)
                }
            )
        }
        composable<Screen.Servicio> {
            val args = it.toRoute<Screen.Servicio>()
            ServicioScreen(
                goBackListScreen = { navHostController.navigate(Screen.ServicioList) },
                servicioId = args.servicioId
            )
        }
        composable<Screen.ArticuloList> {
            ArticuloListScreen(
                onVerArticulo = {
                    navHostController.navigate(Screen.Articulo(it.articuloId ?: 0))
                },
                onAddArticulo = {
                    navHostController.navigate(Screen.Articulo(0))
                },
                goToServicioListScreen = {
                    navHostController.navigate(Screen.ServicioList)
                }
            )
        }
        composable<Screen.Articulo> {
            val args = it.toRoute<Screen.Articulo>()
            ArticuloScreen(
                goBackListScreen = { navHostController.navigate(Screen.ArticuloList) },
                articuloId = args.articuloId
            )
        }
    }
}