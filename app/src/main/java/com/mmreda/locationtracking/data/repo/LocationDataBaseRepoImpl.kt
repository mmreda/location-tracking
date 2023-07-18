package com.mmreda.locationtracking.data.repo

import com.mmreda.locationtracking.data.local.LocationDatabase
import com.mmreda.locationtracking.data.local.entities.LocationEntity
import com.mmreda.locationtracking.domain.repo.LocationDataBaseRepo
import kotlinx.coroutines.flow.Flow

class LocationDataBaseRepoImpl(private val locationDatabase: LocationDatabase): LocationDataBaseRepo {
    override suspend fun getAllLocation(): Flow<List<LocationEntity>> {
        return locationDatabase.locationDao().getAllLocation()
    }

    override suspend fun insertLocation(locationEntity: LocationEntity) {
        locationDatabase.locationDao().insertLocation(locationEntity)
    }

    override suspend fun deleteAllLocation() {
        locationDatabase.locationDao().deleteAllILocation()
    }
}