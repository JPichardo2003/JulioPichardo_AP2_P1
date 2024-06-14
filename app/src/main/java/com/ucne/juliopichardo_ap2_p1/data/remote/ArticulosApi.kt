package com.ucne.juliopichardo_ap2_p1.data.remote

import com.ucne.juliopichardo_ap2_p1.data.remote.dto.ArticulosDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ArticulosApi {
    @GET("api/Articulos")
    suspend fun getArticulos(): List<ArticulosDto>

    @POST("api/Articulos")
    suspend fun addArticulos(@Body articuloDto: ArticulosDto?): Response<ArticulosDto>
}