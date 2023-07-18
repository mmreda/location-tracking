package com.mmreda.locationtracking.data.mapper

import com.google.android.gms.maps.model.LatLng
import com.mmreda.locationtracking.data.local.entities.LocationEntity

fun List<LocationEntity>.toLocationList(): List<LatLng> {
    return map { locationEntity ->
        LatLng(locationEntity.latitude, locationEntity.longitude)
    }
}