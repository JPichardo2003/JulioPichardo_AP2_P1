package com.ucne.juliopichardo_ap2_p1.data.repository

import com.ucne.juliopichardo_ap2_p1.data.remote.ArticulosApi
import com.ucne.juliopichardo_ap2_p1.data.remote.dto.ArticulosDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticulosRepository @Inject constructor(
    private val articulosApi: ArticulosApi
) {
    suspend fun getArticulos(): List<ArticulosDto> {
        return try {
            articulosApi.getArticulos()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun postArticulo(articulo: ArticulosDto) {
        articulosApi.addArticulos(articulo)
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}