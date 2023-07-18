package com.mmreda.locationtracking.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("overview_polyline")
    val overviewPolyline: OverviewPolyline,
)