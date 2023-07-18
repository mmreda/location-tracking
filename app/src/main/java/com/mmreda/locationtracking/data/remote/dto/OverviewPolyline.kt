package com.mmreda.locationtracking.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OverviewPolyline(
    @SerializedName("points") val points: String,
)