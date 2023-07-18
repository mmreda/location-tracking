package com.mmreda.locationtracking.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_database")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val latitude: Double,
    val longitude: Double
)