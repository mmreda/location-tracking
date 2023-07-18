package com.mmreda.locationtracking.domain.usecases

import com.mmreda.locationtracking.domain.repo.LocationDataBaseRepo
import javax.inject.Inject

class DeleteAllLocationUseCase @Inject constructor(
    private val locationDataBaseRepo: LocationDataBaseRepo
) {
    suspend operator fun invoke() {
        locationDataBaseRepo.deleteAllLocation()
    }
}