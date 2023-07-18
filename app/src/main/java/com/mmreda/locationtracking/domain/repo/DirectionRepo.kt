package com.mmreda.locationtracking.domain.repo

import com.mmreda.locationtracking.data.remote.dto.DirectionsResponse

interface DirectionRepo {

    suspend fun getDirection(
        startLocation: String,
        destination: String,
        mode: String,
        apiKey: String
    ): DirectionsResponse
}