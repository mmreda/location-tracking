package com.mmreda.locationtracking.presentation.di

import com.mmreda.locationtracking.data.local.LocationDatabase
import com.mmreda.locationtracking.data.remote.ApiService
import com.mmreda.locationtracking.data.repo.DirectionRepoImpl
import com.mmreda.locationtracking.data.repo.LocationDataBaseRepoImpl
import com.mmreda.locationtracking.domain.repo.DirectionRepo
import com.mmreda.locationtracking.domain.repo.LocationDataBaseRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideDirectionRepo(apiService: ApiService): DirectionRepo {
        return DirectionRepoImpl(apiService)
    }

    @Provides
    fun provideLocationDataBaseRepo(locationDatabase: LocationDatabase): LocationDataBaseRepo {
        return LocationDataBaseRepoImpl(locationDatabase)
    }
}