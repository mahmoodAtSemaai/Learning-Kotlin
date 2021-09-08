package com.webkul.mobikul.odoo.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.FragmentMapBottomSheetBinding
import com.webkul.mobikul.odoo.helper.LocationHelper
import java.io.IOException
import java.util.*

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
class MapBottomSheetFragment : BottomSheetDialogFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    private var mListener: MapLoactionListener? = null
    private var mMap: GoogleMap? = null
    private val TAG = "MapsActivity"

    //   private var mPlacesClient: PlacesClient? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLastKnownLocation: Location? = null
    private val mDefaultLocation = LatLng(28.6294875, 77.3778479)
    private val mDEFAULTZOOM = 15
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted: Boolean = false
    lateinit var mBinding: FragmentMapBottomSheetBinding
    lateinit var addresses: Address
    lateinit var mapFragment: SupportMapFragment
    private var isMarkerInfoVisible = true
    private lateinit var savedAddress: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheetInternal!!).peekHeight = Resources.getSystem().displayMetrics.heightPixels
        }
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_bottom_sheet, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mapFragment = (activity!!.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
        mapFragment.getMapAsync(this)
        //  val apiKey = "AIzaSyB7u5Ils3pj6BZecBtudZYgqCZdsWqDkfg"

        //com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), apiKey)

        // mPlacesClient = com.google.android.libraries.places.api.Places.createClient(this.context!!)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity!!)

        mBinding.buttonSave.setOnClickListener {
            try {
                mListener!!.onSave(addresses)
            } catch (e: java.lang.Exception) {

            }
            dismiss()
        }
        mBinding.cancelButton.setOnClickListener { dismiss() }
    }

    override fun onDetach() {
        mListener = null
        getFragmentManager()!!.beginTransaction().remove(mapFragment).commit();
        super.onDetach()
    }

    override fun onDestroyView() {
        getFragmentManager()!!.beginTransaction().remove(mapFragment).commit();
        super.onDestroyView()
    }

    fun setMapListener(mapLoactionListener: MapLoactionListener) {
        mListener = mapLoactionListener
    }

    interface MapLoactionListener {
        fun onSave(address: Address)
    }

    companion object {
        fun newInstance(savedAddress: String): MapBottomSheetFragment {
            val mapBottomSheetFragment = MapBottomSheetFragment()
            mapBottomSheetFragment.savedAddress = savedAddress
            return mapBottomSheetFragment
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        isMarkerInfoVisible = !isMarkerInfoVisible
        return isMarkerInfoVisible
    }

    override fun onMarkerDragStart(marker: Marker) {
        marker.hideInfoWindow()
    }

    override fun onMarkerDrag(marker: Marker) {

    }

    override fun onMarkerDragEnd(marker: Marker) {
        val latLng = marker.position
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        val geocoder = Geocoder(activity!!.baseContext, Locale.getDefault())
        var title = ""
        var message = ""
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)[0]
            message = addresses.getAddressLine(0)
            title = addresses.featureName
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        marker.title = title
        marker.snippet = message
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
    }

    private lateinit var mMarkerOptions: MarkerOptions
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMarkerOptions = MarkerOptions().position(mDefaultLocation)

        mMarkerOptions.icon(bitmapDescriptorFromVector(this.context!!, R.drawable.ic_vector_map_marker))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(mDefaultLocation))
        mMap!!.setOnMarkerClickListener(this)
        mMap!!.setOnMarkerDragListener(this)
        mMap!!.uiSettings.isZoomControlsEnabled = true
        getLocationPermission()
        pickCurrentPlace()
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun getDeviceLocation() {

        try {
            if (mLocationPermissionGranted) {
                val latLong = LocationHelper.getLatLang(context, savedAddress)
                if (latLong != null) {
                    var location = Location(LocationManager.GPS_PROVIDER)
                    location.latitude = latLong.latitude
                    location.longitude = latLong.longitude
                    setCurrentLocationMarker(location)
                } else {
                    @SuppressLint("MissingPermission") val locationResult = mFusedLocationProviderClient!!.lastLocation
                    locationResult.addOnSuccessListener(this.activity!!) { location ->
                        if (location != null) {
                            setCurrentLocationMarker(location)
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.")
                            mMap!!.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, mDEFAULTZOOM.toFloat()))
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }

    }


    private fun setCurrentLocationMarker(location: Location) {
        // Set the map's camera position to the current location of the device.
        mLastKnownLocation = location
        Log.d(TAG, "Latitude: " + mLastKnownLocation!!.latitude)
        Log.d(TAG, "Longitude: " + mLastKnownLocation!!.longitude)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(mLastKnownLocation!!.latitude,
                        mLastKnownLocation!!.longitude), mDEFAULTZOOM.toFloat()))
        val geocoder = Geocoder(activity!!.baseContext, Locale.getDefault())
        var title = ""
        var message = ""
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)[0]
            message = addresses.getAddressLine(0)
            title = addresses.featureName
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mMap!!.addMarker(mMarkerOptions.position(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)).title(title).snippet(message)).isDraggable = true
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)))
    }


    private fun pickCurrentPlace() {
        if (mMap == null) {
            return
        }
        if (mLocationPermissionGranted) {
            getDeviceLocation()
        } else {
            Log.i(TAG, "The user did not grant location permission.")
            mMap!!.addMarker(MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)))
            getLocationPermission()
        }
    }

    private fun getLocationPermission() {
        mLocationPermissionGranted = false
        if (ContextCompat.checkSelfPermission(context!!.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this.activity!!,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }


}
