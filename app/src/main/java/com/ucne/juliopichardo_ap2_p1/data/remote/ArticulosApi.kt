package com.ucne.juliopichardo_ap2_p1.data.remote

import com.ucne.juliopichardo_ap2_p1.data.remote.dto.ArticuloDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ArticulosApi {
    @GET("api/Articulos/{id}")
    suspend fun getArticulo(@Path("id") id: Int): ArticuloDto
    @GET("api/Articulos")
    suspend fun getArticulos(): List<ArticuloDto>
    @POST("api/Articulos")
    suspend fun addArticulos(@Body articuloDto: ArticuloDto?): Response<ArticuloDto>
    @PUT("api/Articulos/{id}")
    suspend fun updateArticulo(@Path("id") id: Int, @Body articuloDto: ArticuloDto?): Response<ArticuloDto>
    @DELETE("api/Articulos/{id}")
    suspend fun deleteArticulo(@Path("id") id: Int): Response<Unit>
}