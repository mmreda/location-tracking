package com.mmreda.locationtracking.presentation.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.mmreda.locationtracking.data.local.LocationDatabase
import com.mmreda.locationtracking.data.local.dao.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): LocationDatabase {
        return Room.databaseBuilder(
            appContext,
            LocationDatabase::class.java,
            "location_database"
        ).build()
    }

    @Provides
    fun provideChannelDao(locationDatabase: LocationDatabase): LocationDao {
        return locationDatabase.locationDao()
    }
}