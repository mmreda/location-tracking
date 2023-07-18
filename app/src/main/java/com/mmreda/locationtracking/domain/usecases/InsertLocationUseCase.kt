package com.mmreda.locationtracking.domain.usecases

import com.mmreda.locationtracking.data.local.entities.LocationEntity
import com.mmreda.locationtracking.domain.repo.LocationDataBaseRepo
import javax.inject.Inject

class InsertLocationUseCase @Inject constructor(
    private val locationDataBaseRepo: LocationDataBaseRepo
) {
    suspend operator fun invoke(locationEntity: LocationEntity) {
        locationDataBaseRepo.insertLocation(locationEntity)
    }
}