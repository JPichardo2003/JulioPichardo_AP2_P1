package com.ucne.juliopichardo_ap2_p1.presentation.articulo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.juliopichardo_ap2_p1.data.remote.dto.ArticuloDto
import com.ucne.juliopichardo_ap2_p1.data.repository.ArticulosRepository
import com.ucne.juliopichardo_ap2_p1.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticuloViewModel @Inject constructor(
    private val articulosRepository: ArticulosRepository
) : ViewModel() {
    private val articuloId: Int = 0

    private val _uiState = MutableStateFlow(ArticuloUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            //getArticulos()
        }
    }
    fun saveArticulo() {
        viewModelScope.launch {
            try {
                if (uiState.value.articuloId != null) {
                    articulosRepository.updateArticulo(uiState.value.toEntity())
                } else {
                    articulosRepository.addArticulos(uiState.value.toEntity())
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
            _uiState.value = ArticuloUIState()
        }
    }

    fun getArticulos() {
        articulosRepository.getArticulos().onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update {
                    it.copy(isLoading = true)
                }
                is Resource.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        articulos = result.data ?: emptyList()
                    )
                }
                is Resource.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getArticulo(articuloId: Int) {
        viewModelScope.launch {
            val articulo = articulosRepository.getArticulo(articuloId)
            articulo?.let {
                _uiState.update {
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
            _uiState.update {
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
            _uiState.update {
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
        if (descripcionEmpty) {
            _uiState.update { it.copy(descripcionError = "Campo Obligatorio") }
        }
        if (precioEmpty) {
            _uiState.update { it.copy(precioError = "Debe ingresar un precio") }
        }
        return !descripcionEmpty && !precioEmpty
    }
}

data class ArticuloUIState(
    val articuloId: Int? = null,
    var descripcion: String = "",
    var descripcionError: String? = null,
    var precio: Double? = null,
    var precioError: String? = null,
    val articulos: List<ArticuloDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

fun ArticuloUIState.toEntity() = ArticuloDto(
    articuloId = articuloId ?: 0,
    descripcion = descripcion,
    precio = precio ?: 0.0
)