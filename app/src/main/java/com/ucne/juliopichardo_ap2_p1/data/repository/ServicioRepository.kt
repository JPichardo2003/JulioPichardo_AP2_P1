package com.ucne.juliopichardo_ap2_p1.data.repository

import com.ucne.juliopichardo_ap2_p1.data.local.dao.ServicioDao
import com.ucne.juliopichardo_ap2_p1.data.local.entities.ServicioEntity

class ServicioRepository(private val servicioDao: ServicioDao) {
    suspend fun saveServicio(servicio: ServicioEntity) = servicioDao.save(servicio)
    suspend fun deleteServicio(servicio: ServicioEntity) = servicioDao.delete(servicio)
    fun getServicios() = servicioDao.getAll()
    suspend fun getServicio(id: Int) = servicioDao.find(id)
    suspend fun descripcionExist(id: Int, nombre: String): Boolean = servicioDao.descripcionExist(id,nombre)
}