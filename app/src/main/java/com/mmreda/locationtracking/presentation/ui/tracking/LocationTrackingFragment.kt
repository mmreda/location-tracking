package com.mmreda.locationtracking.presentation.ui.tracking

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.mmreda.locationtracking.R
import com.mmreda.locationtracking.data.remote.dto.DirectionsResponse
import com.mmreda.locationtracking.databinding.FragmentLocationTrackingBinding
import com.mmreda.locationtracking.utils.Constants
import com.mmreda.locationtracking.utils.LocationService
import com.mmreda.locationtracking.utils.hasLocationPermission
import com.mmreda.locationtracking.utils.makeToastLong
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LocationTrackingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentLocationTrackingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LocationTrackingViewModel by viewModels()

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var routePoints = emptyList<LatLng>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLocationTrackingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        checkPermissions()
    }

    private fun checkPermissions() {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isNetworkEnabled) {
            requireContext().makeToastLong(getString(R.string.network_is_not_available))
        }

        if (!isGpsEnabled) {
            showOpenSettingsDialog(
                title = getString(R.string.gps_is_required),
                message = getString(R.string.please_enable_gps_in_your_device_settings),
                type = Constants.GPS_DIALOG_TYPE
            )
            return
        }

        if (requireContext().hasLocationPermission()) {
            continueWithMapOperations()
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            )
        }
    }

    private fun showOpenSettingsDialog(title: String, message: String, type: String? = null) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.open_settings)) { _, _ ->
                if (type == Constants.GPS_DIALOG_TYPE) {
                    openGpsSettings()
                } else {
                    openAppSettings()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                checkPermissions()
            }
            .setCancelable(false)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        openAppSettingsLauncher.launch(intent)
    }

    private fun openGpsSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        openAppSettingsLauncher.launch(intent)
    }

    private val openAppSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        onMapReady(googleMap)
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            onMapReady(googleMap)
        } else {
            showOpenSettingsDialog(
                title = getString(R.string.location_permission_required),
                message = getString(R.string.please_allow_all_the_time_location_permission)
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun continueWithMapOperations() {

        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)

                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        userLocation,
                        Constants.DEFAULT_ZOOM
                    )
                )

                binding.buttonDirectionsApi.setOnClickListener {
                    val destination = LatLng(30.032967873473865, 31.41029830683321)

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(userLocation)
                            .title(getString(R.string.start_location))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(destination)
                            .title("Destination")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    )

                    val userLocationString = "${userLocation.latitude},${userLocation.longitude}"
                    val destinationString =
                        "${destination.latitude},${destination.longitude}"

                    val mode = getString(R.string.driving)

                    getDirections(userLocationString, destinationString, mode)
                }

                binding.buttonStartTracking.setOnClickListener {
                    viewModel.deleteAllLocation()
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(userLocation)
                            .title(getString(R.string.start_location))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                    Intent(requireContext(), LocationService::class.java).apply {
                        action = Constants.ACTION_START
                        requireContext().startForegroundService(this)
                    }

                    getLocationFromLocal()
                }

                binding.buttonStopTracking.setOnClickListener {
                    Intent(requireContext(), LocationService::class.java).apply {
                        action = Constants.ACTION_STOP
                        requireContext().startForegroundService(this)
                        googleMap.clear()
                        getLocationFromLocal(showLastRoute = true)
                    }
                }

                binding.buttonShowLastRoute.setOnClickListener {
                    getLocationFromLocal(showLastRoute = true)
                }
            }
        }
    }

    private fun getLocationFromLocal(showLastRoute: Boolean? = false) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                viewModel.getAllLocation().collect { newRoutesPoint ->
                    withContext(Dispatchers.Main) {
                        routePoints = newRoutesPoint
                        googleMap.addPolyline(
                            PolylineOptions()
                                .addAll(routePoints)
                                .color(Color.BLUE)
                                .width(5f)
                        )
                        if (routePoints.isNotEmpty() && showLastRoute == true) {
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(routePoints[0])
                                    .title(getString(R.string.start_location))
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_GREEN
                                        )
                                    )
                            )
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(routePoints[routePoints.size - 1])
                                    .title(getString(R.string.destination))
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_RED
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getDirections(origin: String, destination: String, mode: String) {
        val googleMapApiKye = getString(R.string.maps_api_key)

        lifecycleScope.launch {
            viewModel.getDirection(
                startLocation = origin,
                destination = destination,
                mode = mode,
                googleMapApiKye
            ).collect {
                handleDirectionsResponse(it)
            }
        }
    }

    private fun handleDirectionsResponse(response: DirectionsResponse) {
        val points = response.routes.firstOrNull()?.overviewPolyline?.points
        if (points != null) {
            val decodedPolyline = decodePoly(points)
            googleMap.addPolyline(
                PolylineOptions()
                    .addAll(decodedPolyline)
                    .color(Color.RED)
                    .width(5f)
            )
        }
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dLat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dLng

            val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}