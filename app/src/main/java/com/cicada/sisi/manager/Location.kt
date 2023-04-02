package com.cicada.sisi.manager

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun requestLocationPermission(activity: Activity, requestCode: Int) {
    // Check if location permission has already been granted
    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
        // Permission has already been granted
        return
    }

    // If permission has not been granted, request permission from the user
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
        requestCode
    )
}