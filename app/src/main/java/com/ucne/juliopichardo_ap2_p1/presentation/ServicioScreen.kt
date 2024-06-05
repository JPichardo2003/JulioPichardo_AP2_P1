package com.ucne.juliopichardo_ap2_p1.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.juliopichardo_ap2_p1.R
import com.ucne.juliopichardo_ap2_p1.ui.theme.JulioPichardo_AP2_P1Theme
@Composable
fun ServicioScreen(
    viewModel: ServicioViewModel,
    goBackListScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.servicios.collectAsStateWithLifecycle()

    ServicioBody(
        uiState = uiState,
        onDescripcionChanged = viewModel::onDescripcionChanged,
        onPrecioChanged = viewModel::onPrecioChanged,
        onValidation = viewModel::validation,
        goBackListScreen = goBackListScreen,
        onSaveServicio = {
            viewModel.saveServicio()
        },
        onDeleteServicio = {
            viewModel.deleteServicio()
        },
        onNewServicio = {
            viewModel.newServicio()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioBody(
    uiState: ServicioUIState,
    goBackListScreen: () -> Unit,
    onDescripcionChanged: (String) -> Unit,
    onPrecioChanged: (String) -> Unit,
    onSaveServicio: () -> Unit,
    onDeleteServicio: () -> Unit,
    onNewServicio: () -> Unit,
    onValidation: () -> Boolean
) {
    var guardo by remember { mutableStateOf(false) }
    var errorGuardar by remember { mutableStateOf(false) }
    var elimino by remember { mutableStateOf(false) }
    var errorEliminar by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Registro Servicio") },
                navigationIcon = {
                    IconButton(onClick = goBackListScreen) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Lista"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion,
                        onValueChange = onDescripcionChanged,
                        isError = uiState.descripcionError != null,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Campo Descripción"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (uiState.descripcionError != null) {
                        Text(
                            text = uiState.descripcionError ?: "",
                            color = Color.Red,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp
                        )
                    }

                    OutlinedTextField(
                        label = { Text(text = "Precio") },
                        value = uiState.precio.toString().replace("null", ""),
                        placeholder = { Text(text = "0.0") },
                        prefix = { Text(text = "$") },
                        onValueChange = onPrecioChanged,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.icons8dollarblack),
                                contentDescription = "Campo Descripción"
                            )
                        },
                        isError = uiState.precioError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (uiState.precioError != null) {
                        Text(
                            text = uiState.precioError ?: "",
                            color = Color.Red,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.padding(2.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { onNewServicio() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "new button"
                            )
                            Text( text = "New" )
                        }
                        OutlinedButton(
                            onClick = {
                                if (onValidation()) {
                                    onSaveServicio()
                                    guardo = true
                                    goBackListScreen()
                                } else {
                                    errorGuardar = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "save button"
                            )
                            Text(text = "Save")
                        }

                        OutlinedButton(
                            onClick = {
                                if (uiState.servicioId != null) {
                                    showDialog = true
                                } else {
                                    errorEliminar = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete button"
                            )
                            Text(text = "Delete")
                        }
                        if (elimino) {
                            Notification("Eliminado Correctamente")
                            elimino = false
                        }
                        if (errorEliminar) {
                            Notification("Error al Eliminar")
                            errorEliminar = false
                        }
                        if (guardo) {
                            Notification("Guardado Correctamente")
                            guardo = false
                        }
                        if (errorGuardar) {
                            Notification("Error al Guardar")
                            errorGuardar = false
                        }
                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Eliminar Servicio") },
                            text = { Text("¿Está seguro de que desea eliminar este servicio?") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        onDeleteServicio()
                                        showDialog = false
                                        elimino = true
                                        goBackListScreen()
                                    }
                                ) {
                                    Text("Sí")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDialog = false }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Notification(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_LONG).show()
}
@Preview
@Composable
private fun ServicioPreview() {
    JulioPichardo_AP2_P1Theme {
        ServicioBody(
            uiState = ServicioUIState(),
            onDescripcionChanged = {},
            onPrecioChanged = {},
            onValidation = { false },
            onSaveServicio = {},
            onDeleteServicio = {},
            onNewServicio = {},
            goBackListScreen = {}
        )
    }
}