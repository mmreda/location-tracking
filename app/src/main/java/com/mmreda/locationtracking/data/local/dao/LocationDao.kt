package com.mmreda.locationtracking.data.local.dao

import androidx.room.*
import com.mmreda.locationtracking.data.local.entities.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM location_database ORDER BY id ASC")
    fun getAllLocation(): Flow<List<LocationEntity>>

    @Update
    fun updateLocation(location: LocationEntity)

    @Query("DELETE FROM location_database")
    fun deleteAllILocation()
}