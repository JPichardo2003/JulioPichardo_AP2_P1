package com.ucne.juliopichardo_ap2_p1.presentation.articulo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.juliopichardo_ap2_p1.data.remote.dto.ArticulosDto
import com.ucne.juliopichardo_ap2_p1.data.repository.ArticulosRepository
import com.ucne.juliopichardo_ap2_p1.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticuloViewModel @Inject constructor(
    private val articulosRepository: ArticulosRepository
) : ViewModel() {
    private val articuloId: Int = 0
    var uiState = MutableStateFlow(ArticulosUIState())
        private set

    init {
        viewModelScope.launch {
            /*val articulo = articulosRepository.getArticulo(articuloId)
            articulo?.let {
                uiState.update {
                    it.copy(
                        articuloId = articulo.articuloId,
                        descripcion = articulo.descripcion,
                        precio = articulo.precio
                    )
                }
            }*/
            getArticulos()
        }
    }
    fun saveArticulo() {
        viewModelScope.launch {
            try {
                if (uiState.value.articuloId != null) {
                    articulosRepository.updateArticulo(uiState.value.toEntity())
                } else {
                    articulosRepository.addArticulos(uiState.value.toEntity())
                    //newArticulo()
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun deleteArticulo() {
        viewModelScope.launch {
            articulosRepository.deleteArticulo(uiState.value.articuloId ?: 0)
        }
    }

    fun newArticulo() {
        viewModelScope.launch {
            uiState.value = ArticulosUIState()
        }
    }

    fun getArticulos() {
        articulosRepository.getArticulos().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    uiState.update { it.copy(isLoading = true) }
                    delay(2_000)
                }
                is Resource.Success -> {
                    uiState.update {
                        it.copy(
                            isLoading = false,
                            articulos = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Error -> {
                    uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
    fun getArticulo(articuloId: Int) {
        viewModelScope.launch {
            val articulo = articulosRepository.getArticulo(articuloId)
            articulo?.let {
                uiState.update {
                    it.copy(
                        articuloId = articulo.articuloId,
                        descripcion = articulo.descripcion,
                        precio = articulo.precio
                    )
                }
            }
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
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

fun ArticulosUIState.toEntity() = ArticulosDto(
    articuloId = articuloId ?: 0,
    descripcion = descripcion,
    precio = precio ?: 0.0
)