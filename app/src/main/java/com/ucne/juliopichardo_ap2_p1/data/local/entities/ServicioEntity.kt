package com.ucne.juliopichardo_ap2_p1.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Servicios")
data class ServicioEntity(
    @PrimaryKey
    val servicioId: Int? = null,
    var descripcion: String? = "",
    var precio: Double? = 0.0
)
