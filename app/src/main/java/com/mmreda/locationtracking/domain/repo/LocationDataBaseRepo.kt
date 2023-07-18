package com.mmreda.locationtracking.domain.repo

import com.mmreda.locationtracking.data.local.entities.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationDataBaseRepo {

    suspend fun getAllLocation(): Flow<List<LocationEntity>>

    suspend fun insertLocation(locationEntity: LocationEntity)


    suspend fun deleteAllLocation()
}