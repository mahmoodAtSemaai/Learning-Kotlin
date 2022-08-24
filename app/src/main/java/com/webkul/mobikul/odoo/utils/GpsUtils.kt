package com.webkul.mobikul.odoo.utils

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemAddressLocationDialogBinding


class GpsUtils(private val gpsUtilsInterface: GpsUtilsInterface) {
    var sentLocation = false
    var locationDailogClicked = false
    val MY_PERMISSIONS_REQUEST_LOCATION = 101
    fun requestAccessFineLocationPermission(activity: Activity, requestId: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            requestId
        )
    }

    fun isAccessFineLocationGranted(context: Context): Boolean {
        return ContextCompat
            .checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun showGPSNotEnabledDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.enable_gps))
        builder.setMessage(context.getString(R.string.required_for_this_app)).setCancelable(false)
        builder.setPositiveButton(context.getString(R.string.yes)) { _, _ -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.background_orange))
    }

    fun showLocationDialog(context: Context,activity: Activity) {
        val dialog = Dialog(context)
        val dialogBinding: ItemAddressLocationDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_address_location_dialog,
            null,
            false
        )
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if(locationDailogClicked){
            checkPermission(context, activity)
        }else{
            dialog.show()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialogBinding.btnLocationContinue.setOnClickListener {
                dialog.dismiss()
                locationDailogClicked = true
                checkPermission(context, activity)
            }
        }
    }

    fun checkPermission(context: Context,activity: Activity) {
        when {
            isAccessFineLocationGranted(context) -> {
                when {
                    isLocationEnabled(context) -> setUpLocationListener(context, activity)
                    else -> showGPSNotEnabledDialog(context)
                }
            }
            else -> requestAccessFineLocationPermission(activity, MY_PERMISSIONS_REQUEST_LOCATION)
        }
    }

    fun setUpLocationListener(context: Context,activity: Activity) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)
        val locationRequest = LocationRequest().setInterval(500).setFastestInterval(500)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        if(sentLocation.not()){
                            gpsUtilsInterface.location(context.getString(R.string.location_while_using_the_app),location.latitude,location.longitude)
                            sentLocation = true
                        }
                    }
                }
            }, Looper.myLooper()
        )
    }

    fun permissionDenied(context: Context){
        gpsUtilsInterface.location(context.getString(R.string.location_deny),0.000,0.000)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        activity: Activity,
        fragment : Fragment
    ) {
        if(requestCode == MY_PERMISSIONS_REQUEST_LOCATION){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                when {
                    isLocationEnabled(activity) -> {
                        setUpLocationListener(fragment.requireContext(),activity)
                    }
                    else -> {
                        showGPSNotEnabledDialog(activity)
                    }
                }
            } else {
                permissionDenied(activity)
            }
        }
    }
}

interface GpsUtilsInterface{
    fun location(locationType : String,latitude : Double, longitude : Double)
}