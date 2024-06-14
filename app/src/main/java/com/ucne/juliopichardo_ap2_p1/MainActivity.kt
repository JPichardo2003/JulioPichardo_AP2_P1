package com.ucne.juliopichardo_ap2_p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.ucne.juliopichardo_ap2_p1.data.local.database.ServicioDb
import com.ucne.juliopichardo_ap2_p1.data.repository.ServicioRepository
import com.ucne.juliopichardo_ap2_p1.presentation.navigation.Parcial1NavHost
import com.ucne.juliopichardo_ap2_p1.ui.theme.JulioPichardo_AP2_P1Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            JulioPichardo_AP2_P1Theme {
                val navHostController = rememberNavController()
                Parcial1NavHost(navHostController)
            }
        }
    }
}