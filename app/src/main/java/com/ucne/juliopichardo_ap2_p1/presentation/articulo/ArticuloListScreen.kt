package com.ucne.juliopichardo_ap2_p1.presentation.articulo

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.juliopichardo_ap2_p1.data.remote.dto.ArticuloDto
import com.ucne.juliopichardo_ap2_p1.ui.theme.JulioPichardo_AP2_P1Theme
import com.ucne.juliopichardo_ap2_p1.ui.theme.Purple40

@Composable
fun ArticuloListScreen(
    viewModel: ArticuloViewModel = hiltViewModel(),
    onVerArticulo: (ArticuloDto) -> Unit,
    onAddArticulo: () -> Unit,
    goToServicioListScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArticuloListBody(
        uiState = uiState,
        articulos = uiState.articulos,
        onAddArticulo = onAddArticulo,
        onVerArticulo = onVerArticulo,
        goToServicioListScreen = goToServicioListScreen
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticuloListBody(
    uiState: ArticuloUIState,
    viewModel: ArticuloViewModel = hiltViewModel(),
    articulos: List<ArticuloDto>,
    onAddArticulo: () -> Unit,
    onVerArticulo: (ArticuloDto) -> Unit,
    goToServicioListScreen: () -> Unit
) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Articles")

                        TextButton(
                            onClick = { viewModel.getArticulos() }
                        ) {
                            Text(text = "Get Articles", color = Purple40)
                        }

                        TextButton(
                            onClick = { goToServicioListScreen() }
                        ) {
                            Text(text = "Get Services", color = Purple40)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddArticulo) {
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
                Text(text = "Descripción", modifier = Modifier.weight(0.300f))
                Text(text = "Precio", modifier = Modifier.weight(0.30f))
            }

            Divider()

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            uiState.errorMessage?.let {
                Text(text = it, color = Color.Red)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(articulos) { articulo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVerArticulo(articulo) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = articulo.articuloId.toString(), modifier = Modifier.weight(0.10f))
                        Text(text = articulo.descripcion, modifier = Modifier.weight(0.300f))
                        Text(text = articulo.precio.toString(), modifier = Modifier.weight(0.30f))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ArticuloListPreview() {
    val articulos = listOf(
        ArticuloDto(
            articuloId = 1,
            descripcion = "Servicio 1",
            precio = 10.0
        ),
    )
    JulioPichardo_AP2_P1Theme{
        ArticuloListBody(
            articulos = articulos,
            onAddArticulo = {},
            onVerArticulo = {},
            goToServicioListScreen = {},
            uiState = ArticuloUIState()
        )
    }
}