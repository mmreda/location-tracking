package com.mmreda.locationtracking.domain.usecases

import com.google.android.gms.maps.model.LatLng
import com.mmreda.locationtracking.data.mapper.toLocationList
import com.mmreda.locationtracking.domain.repo.LocationDataBaseRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllLocationUseCase @Inject constructor(
    private val locationDataBaseRepo: LocationDataBaseRepo
) {
    suspend operator fun invoke(): Flow<List<LatLng>> {
        return locationDataBaseRepo.getAllLocation().map {  locationList ->
            locationList.toLocationList()
        }
    }
}