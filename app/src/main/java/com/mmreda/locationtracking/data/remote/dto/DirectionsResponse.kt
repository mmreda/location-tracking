package com.mmreda.locationtracking.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DirectionsResponse(
    @SerializedName("routes")
    val routes: List<Route>,
)