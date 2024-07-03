package com.ucne.juliopichardo_ap2_p1.presentation.servicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.juliopichardo_ap2_p1.data.local.entities.ServicioEntity
import com.ucne.juliopichardo_ap2_p1.data.repository.ServicioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ServicioViewModel @Inject constructor(
    private val repository: ServicioRepository
) : ViewModel() {

    var uiState = MutableStateFlow(ServicioUIState())
        private set

    val servicios = repository.getServicios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            //getServicio(servicioId)
        }
    }

    fun onDescripcionChanged(descripcion: String) {
        if (!descripcion.startsWith(" ")) {
            uiState.update {
                it.copy(
                    descripcion = descripcion,
                    descripcionError = null
                )
            }
        }
    }

    fun onPrecioChanged(precioStr: String) {
        val regex = Regex("[0-9]{0,7}\\.?[0-9]{0,2}")
        if (precioStr.matches(regex)) {
            val total = precioStr.toDoubleOrNull() ?: 0.0
            uiState.update {
                it.copy(
                    precio = total,
                    precioError = null
                )
            }
        }
    }

    fun getServicio(servicioId: Int) {
        viewModelScope.launch {
            val servicio = repository.getServicio(servicioId)
            servicio?.let {
                uiState.update {
                    it.copy(
                        servicioId = servicio.servicioId,
                        descripcion = servicio.descripcion ?: "",
                        precio = servicio.precio
                    )
                }
            }
        }
    }

    fun saveServicio() {
        viewModelScope.launch {
            repository.saveServicio(uiState.value.toEntity())
        }
    }

    fun deleteServicio() {
        viewModelScope.launch {
            repository.deleteServicio(uiState.value.toEntity())
        }
    }

    fun newServicio() {
        viewModelScope.launch {
            uiState.value = ServicioUIState()
        }
    }

    fun validation(): Boolean {
        val descripcionEmpty = uiState.value.descripcion.isEmpty()
        val precioEmpty = (uiState.value.precio ?: 0.0) <= 0.0
        val descripcionExists = runBlocking { descripcionExists() }
        if (descripcionExists) {
            uiState.update { it.copy(descripcionError = "Ya existe un servicio con esa descripción") }
        }
        if (descripcionEmpty) {
            uiState.update { it.copy(descripcionError = "Campo Obligatorio") }
        }
        if (precioEmpty) {
            uiState.update { it.copy(precioError = "Debe ingresar un precio") }
        }
        return !descripcionEmpty && !precioEmpty && !descripcionExists
    }

    private suspend fun descripcionExists(): Boolean {
        return repository.descripcionExist(uiState.value.servicioId ?: 0, uiState.value.descripcion)
    }
}

data class ServicioUIState(
    val servicioId: Int? = null,
    var descripcion: String = "",
    var descripcionError: String? = null,
    var precio: Double? = null,
    var precioError: String? = null
)

fun ServicioUIState.toEntity() = ServicioEntity(
    servicioId = servicioId,
    descripcion = descripcion,
    precio = precio
)
