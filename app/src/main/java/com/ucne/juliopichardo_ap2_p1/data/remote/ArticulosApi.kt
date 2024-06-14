package com.ucne.juliopichardo_ap2_p1.data.remote

import com.ucne.juliopichardo_ap2_p1.data.remote.dto.ArticulosDto
import retrofit2.http.GET

interface ArticulosApi {
    @GET("api/Articulos")
    suspend fun getArticulos(): List<ArticulosDto>
}