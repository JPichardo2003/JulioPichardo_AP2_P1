package com.ucne.juliopichardo_ap2_p1.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.juliopichardo_ap2_p1.data.local.entities.ServicioEntity
import com.ucne.juliopichardo_ap2_p1.ui.theme.JulioPichardo_AP2_P1Theme
@Composable
fun ServicioListScreen(
    viewModel: ServicioViewModel,
    onVerServicio: (ServicioEntity) -> Unit,
    onAddServicio: () -> Unit
) {
    val servicios by viewModel.servicios.collectAsStateWithLifecycle()
    ServicioListBody(
        servicios = servicios,
        onAddServicio = onAddServicio,
        onVerServicio = onVerServicio
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioListBody(
    servicios: List<ServicioEntity>,
    onAddServicio: () -> Unit,
    onVerServicio: (ServicioEntity) -> Unit
) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Servicios") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddServicio) {
                Icon(Icons.Filled.Add, "Agregar nueva entidad")
            }
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .padding(it)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "ID", modifier = Modifier.weight(0.10f))
                Text(text = "Descripción", modifier = Modifier.weight(0.350f))
                Text(text = "Precio", modifier = Modifier.weight(0.25f))
            }

            Divider()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(servicios) { servicio ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVerServicio(servicio) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = servicio.servicioId.toString(), modifier = Modifier.weight(0.12f))
                        Text(text = servicio.descripcion.toString(), modifier = Modifier.weight(0.420f))
                        Text(text = servicio.precio.toString(), modifier = Modifier.weight(0.30f))
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun ServicioListPreview() {
    val servicios = listOf(
        ServicioEntity(
            servicioId = 1,
            descripcion = "RAM",
            precio = 34.4
        )
    )
    JulioPichardo_AP2_P1Theme{
        ServicioListBody(
            servicios = servicios,
            onAddServicio = {},
            onVerServicio = {}
        )
    }
}