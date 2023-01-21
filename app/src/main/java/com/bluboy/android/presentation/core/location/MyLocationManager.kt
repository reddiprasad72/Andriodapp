package com.bluboy.android.presentation.core.location

import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.pixplicity.easyprefs.library.Prefs
import com.bluboy.android.presentation.utility.Logger
import com.bluboy.android.presentation.utility.PrefKeys
import io.reactivex.disposables.CompositeDisposable


open class MyLocationManager constructor(private val context: Context) {

    var myLocationManager: MyLocationListener? = null
    var compositeDisposable = CompositeDisposable()
    var locationRequest: LocationRequest? = null
    var liveLocationList: MutableList<String> = arrayListOf()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10000)

//        val builder = LocationSettingsRequest.Builder()
//        builder.addLocationRequest(locationRequest!!)
//        val locationSettingsRequest = builder.build()
//        val settingsClient = LocationServices.getSettingsClient(context)
//        settingsClient.checkLocationSettings(locationSettingsRequest)
    }

    fun addLiveLocationUser(id: String) {
        if (id.isNotEmpty() && !liveLocationList.contains(id)) {
            liveLocationList.add(id)
        }
    }

    fun removeLiveLocationUser(id: String) {
        if (id.isNotEmpty() && liveLocationList.contains(id)) {
            liveLocationList.remove(id)
        }
    }

    fun startLocationLive() {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            Logger.d("Live location sharing")

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        Logger.d("Live location sharing $location")
                        myLocationManager?.onLocationReceived(location)
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun startLocation() {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            Logger.d("Location sharing")

            Logger.d("Location sharing")
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        Logger.d("Live location sharing $location")
                        Prefs.putString(PrefKeys.Latitude, location?.latitude?.toString())
                        Prefs.putString(PrefKeys.Longitude, location?.longitude?.toString())
                        myLocationManager?.onLocationReceived(location)
                        stopLocation()
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            Logger.d("Location permission denied")
        }
    }

    fun stopLocation() {
        if (liveLocationList?.isEmpty()) {
            onClear()
        }
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    //@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onClear() {
        Logger.d("Location sharing off")
        compositeDisposable.clear()
    }

}