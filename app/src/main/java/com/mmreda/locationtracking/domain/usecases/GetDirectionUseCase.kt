package com.mmreda.locationtracking.domain.usecases

import com.mmreda.locationtracking.data.remote.dto.DirectionsResponse
import com.mmreda.locationtracking.domain.repo.DirectionRepo
import javax.inject.Inject

class GetDirectionUseCase @Inject constructor(
    private val directionRepo: DirectionRepo,
) {

    suspend operator fun invoke(
        startLocation: String,
        destination: String,
        mode: String,
        apiKey: String,
    ): DirectionsResponse {
        return directionRepo.getDirection(
            startLocation = startLocation,
            destination = destination,
            mode = mode,
            apiKey = apiKey
        )
    }
}