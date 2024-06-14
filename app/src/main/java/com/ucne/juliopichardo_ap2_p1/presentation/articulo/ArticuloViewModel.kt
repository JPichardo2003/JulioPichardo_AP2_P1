package com.ucne.juliopichardo_ap2_p1.presentation.articulo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.juliopichardo_ap2_p1.data.remote.dto.ArticulosDto
import com.ucne.juliopichardo_ap2_p1.data.repository.ArticulosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ArticuloViewModel @Inject constructor(
    private val articulosRepository: ArticulosRepository
) : ViewModel() {
    private var articuloId: Int = 0
    var uiState = MutableStateFlow(ArticulosUIState())
        private set

    init {
        viewModelScope.launch {
            /*val servicio = articulosRepository.getServicio(articuloId)

            servicio?.let {
                uiState.update {
                    it.copy(
                        servicioId = servicio.servicioId,
                        descripcion = servicio.descripcion ?: "",
                        precio = servicio.precio
                    )
                }
            }*/
            //getArticulos()
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

    fun saveServicio() {
        viewModelScope.launch {
            try {
                articulosRepository.postArticulo(uiState.value.toEntity())
                //newServicio()
            }catch (e: Exception){
                e.printStackTrace()
            }
            //repository.saveServicio(uiState.value.toEntity())
        }
    }

    fun deleteServicio() {
        viewModelScope.launch {
            //repository.deleteServicio(uiState.value.toEntity())
        }
    }

    fun newServicio() {
        viewModelScope.launch {
            uiState.value = ArticulosUIState()
        }
    }

    fun getArticulos() {
        viewModelScope.launch {
            val articulos = articulosRepository.getArticulos()
            uiState.update {
                it.copy(articulos = articulos)
            }
        }
    }

    fun validation(): Boolean {
        val descripcionEmpty = uiState.value.descripcion.isEmpty()
        val precioEmpty = (uiState.value.precio ?: 0.0) <= 0.0
        /*val descripcionExists = runBlocking { descripcionExists() }
        if (descripcionExists) {
            uiState.update { it.copy(descripcionError = "Ya existe un servicio con esa descripción") }
        }*/
        if (descripcionEmpty) {
            uiState.update { it.copy(descripcionError = "Campo Obligatorio") }
        }
        if (precioEmpty) {
            uiState.update { it.copy(precioError = "Debe ingresar un precio") }
        }
        return !descripcionEmpty && !precioEmpty
    }

    /*private suspend fun descripcionExists(): Boolean {
        return articulosRepository.descripcionExist(uiState.value.servicioId ?: 0, uiState.value.descripcion)
    }*/
}

data class ArticulosUIState(
    val articuloId: Int? = null,
    var descripcion: String = "",
    var descripcionError: String? = null,
    var precio: Double? = null,
    var precioError: String? = null,
    val articulos: List<ArticulosDto> = emptyList(),
)

fun ArticulosUIState.toEntity() = ArticulosDto(
    articuloId = articuloId ?: 0,
    descripcion = descripcion,
    precio = precio ?: 0.0
)