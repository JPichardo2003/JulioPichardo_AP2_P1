package com.ucne.juliopichardo_ap2_p1.data.repository

import com.ucne.juliopichardo_ap2_p1.data.remote.ArticulosApi
import com.ucne.juliopichardo_ap2_p1.data.remote.dto.ArticulosDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticulosRepository @Inject constructor(
    private val articulosApi: ArticulosApi
) {
    fun getArticulos(): Flow<Resource<List<ArticulosDto>>> = flow {
        emit(Resource.Loading())
        try {
            val articulos = articulosApi.getArticulos()
            emit(Resource.Success(articulos))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }
    suspend fun getArticulo(id: Int): ArticulosDto? {
        return try {
            articulosApi.getArticulo(id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addArticulos(articulo: ArticulosDto) {
        articulosApi.addArticulos(articulo)
    }

    suspend fun updateArticulo(articulo: ArticulosDto) {
        articulosApi.updateArticulo(articulo.articuloId, articulo)
    }

    suspend fun deleteArticulo(id: Int) {
        articulosApi.deleteArticulo(id)
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}