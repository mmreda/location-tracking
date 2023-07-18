package com.mmreda.locationtracking.data.repo

import com.mmreda.locationtracking.data.remote.ApiService
import com.mmreda.locationtracking.data.remote.dto.DirectionsResponse
import com.mmreda.locationtracking.domain.repo.DirectionRepo

class DirectionRepoImpl(private val apiService: ApiService) : DirectionRepo {
    override suspend fun getDirection(
        startLocation: String,
        destination: String,
        mode: String,
        apiKey: String
    ): DirectionsResponse {
        return apiService.getDirections(
            startLocation =  startLocation,
            destination = destination,
            mode = mode,
            apiKey = apiKey
        )
    }
}