package com.cicada.sisi.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.FragmentActivity
import com.cicada.sisi.R
import com.cicada.sisi.animation.animateText
import com.cicada.sisi.bot.search
import com.cicada.sisi.data.EcoPlace
import com.cicada.sisi.isDouble
import com.cicada.sisi.manager.requestLocationPermission
import com.cicada.sisi.removePartFromString
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import java.lang.Error

class GMap : Fragment(), OnMapReadyCallback {

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var map : GoogleMap
    private var responseFromAI = false

    private val oldCenterLat = 45.7536
    private val oldCenterLong = 21.2289

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let { requestLocationPermission(it, REQUEST_LOCATION_PERMISSION) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_g_map, container, false)
        val context = view.context // best way to get context
        val searchBar : SearchView = view.findViewById(R.id.searchBar)
        val botText : TextView = view.findViewById(R.id.botText)

        attachMapFragment()

        try {

            attachSearchListener(searchBar, botText, activity)

        } catch (error: Error){

            error.message?.let { Snackbar.make(view, it, Snackbar.LENGTH_LONG) }
        }

        return view
    }

    private fun attachSearchListener(
        searchBar: SearchView,
        botText: TextView,
        activity: FragmentActivity?
    ) {

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Do something when the user submits the search query

                if ((query != null || query?.length!! > 0) && !responseFromAI) {

                    responseFromAI = true
                    loadingStateForBot()

                    search(query) {

                        activity?.let { it1 -> botText.animateText(
                            "Your results: \n" + it.joinToString(separator = "\n"), it1
                        )

                            val list = mutableListOf<EcoPlace>()

                            extractValuesForEcoPlaceList(it, list)

                            val markerView = LayoutInflater.from(searchBar.context).inflate(R.layout.map_marker, null)
                            val markerImage = markerView.findViewById<ImageView>(R.id.marker_image)
                            // val locationName = markerView.findViewById<TextView>(R.id.location_name)

                            val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                            markerView.measure(widthSpec, heightSpec)
                            markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)


                            markerImage.setImageResource(R.drawable.ic_eco)

                            clearMap(activity)

                            list.forEach { ecoPlace ->

                              //  locationName.text = ecoPlace.name

                                val markerOptions = MarkerOptions()
                                    .position(LatLng(ecoPlace.lat, ecoPlace.long))
                                    .icon(BitmapDescriptorFactory.fromBitmap(markerView.toBitmap()))
                                    .title(ecoPlace.name)

                                activity.runOnUiThread {

                                    map.addMarker(markerOptions)
                                }
                            }

                            moveCameraToTheFirstMarkerOnTheMap(list, activity)
                        }
                    }
                }

                return true
            }

            private fun loadingStateForBot() {
                activity?.let { botText.animateText("Loading...", it) }
            }

            private fun clearMap(activity: FragmentActivity) {
                activity.runOnUiThread {
                    map.clear()
                }
            }

            private fun extractValuesForEcoPlaceList(
                it: List<String>,
                list: MutableList<EcoPlace>
            ) {
                it.forEach { place ->

                    val (namePlace, lat, long) = extractEcoPlacefromPromtData(place)
                    list.add(
                        EcoPlace(
                            name = namePlace,
                            lat = lat,
                            long = long
                        )
                    )
                }
            }

            private fun moveCameraToTheFirstMarkerOnTheMap(
                list: MutableList<EcoPlace>,
                activity: FragmentActivity
            ) {

                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(list[0].lat, list[0].long))
                    .zoom(13.5f)
                    .build()

                activity.runOnUiThread {


                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }

                responseFromAI = false
            }

            private fun extractEcoPlacefromPromtData(place: String): Triple<String, Double, Double> {

                val index = place.indexOfFirst {
                    it == ',' || it == '-' || it == ':'
                }

                val placeWithoutName = removePartFromString(place, 0, index).trim()
                val indexSecond = placeWithoutName.indexOfFirst {
                    it == ',' || it == '-' || it == ':'
                }

                val namePlace = place.substring(0, index).trim()
                val trim = placeWithoutName.substring(0, indexSecond).trim()
                val lat : Double = if(trim.isDouble()) {

                    trim.toDouble()
                } else {

                    oldCenterLat
                }

                val long : Double = if(placeWithoutName.substring(
                        indexSecond + 1,
                        placeWithoutName.length
                    ).trim().isDouble()) {

                    placeWithoutName.substring(
                        indexSecond + 1,
                        placeWithoutName.length
                    ).trim().toDouble()
                } else {

                    oldCenterLong
                }

                return Triple(namePlace, lat, long)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Do something when the user changes the search query

                // -->> Redundant
                return true
            }
        })
    }

    fun View.toBitmap(): Bitmap {

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }


    private fun attachMapFragment() {

        val mapFragment = childFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment

        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap

        moveMapCameraToUserLocation(googleMap)
        loadDarkStyle(googleMap)
    }

    private fun moveMapCameraToUserLocation(
        googleMap: GoogleMap
    ) {

        val zoomLevel = 15f

        context?.let {
            getCurrentLocation(it) { latitude, longitude ->

                // Handle location updates
                Log.d("Location", "Latitude: $latitude, Longitude: $longitude")

                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng
                            (latitude, longitude),
                        zoomLevel
                    )
                )
            }
        }
    }

    private fun loadDarkStyle(googleMap: GoogleMap) {
        // load dark style
        val success = googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), R.raw.dark
            )
        )

        Log.d("Map style boolean", "$success")
    }

    private fun getCurrentLocation(context: Context, onLocationReceived:
        (latitude: Double, longitude: Double) -> Unit) {

        // Default info for the Old Center of Timisioara

        // Get the FusedLocationProviderClient
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Request location updates
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // Handle location updates
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        onLocationReceived(latitude, longitude)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle location request failure

                    onLocationReceived(oldCenterLat, oldCenterLong) // send default data
                    Log.e("Location", "Error getting location", exception)
                }
        } else {

            onLocationReceived(oldCenterLat, oldCenterLong)

            activity?.let {
                requestLocationPermission(it, REQUEST_LOCATION_PERMISSION)
            }
        }
    }

}