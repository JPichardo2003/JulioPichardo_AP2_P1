package com.ucne.juliopichardo_ap2_p1.domain.location

import android.location.Location
interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}
