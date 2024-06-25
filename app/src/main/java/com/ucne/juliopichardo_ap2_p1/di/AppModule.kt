package com.ucne.juliopichardo_ap2_p1.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ucne.juliopichardo_ap2_p1.data.local.database.ServicioDb
import com.ucne.juliopichardo_ap2_p1.data.location.DefaultLocationTracker
import com.ucne.juliopichardo_ap2_p1.data.remote.ArticulosApi
import com.ucne.juliopichardo_ap2_p1.domain.location.LocationTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideServicioDb(@ApplicationContext appContext: Context): ServicioDb {
        return Room.databaseBuilder(
            appContext,
            ServicioDb::class.java,
            "Servicio.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideServicioDao(database: ServicioDb) = database.servicioDao()

    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesArticulosApi(moshi: Moshi): ArticulosApi {
        return Retrofit.Builder()
            .baseUrl("https://articuloswebapi.azurewebsites.net/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ArticulosApi::class.java)
    }

    //location
    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        application: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): LocationTracker = DefaultLocationTracker(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application
    )
}