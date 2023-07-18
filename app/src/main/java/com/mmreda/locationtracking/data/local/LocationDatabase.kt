package com.mmreda.locationtracking.data.local

import androidx.room.*
import com.mmreda.locationtracking.data.local.dao.LocationDao
import com.mmreda.locationtracking.data.local.entities.LocationEntity

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}