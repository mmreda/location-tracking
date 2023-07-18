package com.mmreda.locationtracking.data.remote

import com.mmreda.locationtracking.data.remote.dto.DirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") startLocation: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") apiKey: String
    ): DirectionsResponse
}