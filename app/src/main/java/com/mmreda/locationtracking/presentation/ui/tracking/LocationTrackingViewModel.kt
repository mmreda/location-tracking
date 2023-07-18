package com.mmreda.locationtracking.presentation.ui.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.mmreda.locationtracking.domain.usecases.DeleteAllLocationUseCase
import com.mmreda.locationtracking.domain.usecases.GetAllLocationUseCase
import com.mmreda.locationtracking.domain.usecases.GetDirectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationTrackingViewModel @Inject constructor(
    private val getDirectionUseCase: GetDirectionUseCase,
    private val getAllLocationUseCase: GetAllLocationUseCase,
    private val deleteAllLocationUseCase: DeleteAllLocationUseCase,
) : ViewModel() {

    fun getDirection(
        startLocation: String,
        destination: String,
        mode: String,
        apiKey: String,
    ) = flow {
        try {
            emit(
                getDirectionUseCase.invoke(
                    startLocation = startLocation,
                    destination = destination,
                    mode = mode,
                    apiKey = apiKey
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllLocation(): Flow<List<LatLng>> = flow {
        try {
            emitAll(getAllLocationUseCase.invoke())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun deleteAllLocation() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            deleteAllLocationUseCase.invoke()
        }
    }
}