package com.mmreda.locationtracking.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.mmreda.locationtracking.data.local.entities.LocationEntity
import com.mmreda.locationtracking.domain.usecases.InsertLocationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LocationService() : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private var isServiceStarted = false

    @Inject
    lateinit var insertLocationUseCase: InsertLocationUseCase

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationClient(
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            "Location Tracking",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START -> start()
            Constants.ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        if (!isServiceStarted) {
            val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Tracking location...")
                .setContentText("Don't remove app from background")
                .build()

            val intervalTimeInMillis: Long = 5000
            locationClient
                .getLocationUpdates(intervalTimeInMillis)
                .catch { e -> e.printStackTrace() }
                .onEach { location ->
                    val locationEntity = LocationEntity(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    insertLocationUseCase.invoke(locationEntity)
                }
                .launchIn(serviceScope)

            startForeground(1, notification)
            isServiceStarted = true
        }
    }

    private fun stop() {
        if (isServiceStarted) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            isServiceStarted = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}