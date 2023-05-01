package com.example.ieum

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ieum.databinding.ActivityMapsBinding
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CircleOptions
import kotlin.random.Random


class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var geofencingClient: GeofencingClient
    private lateinit var markingOptions: MarkerOptions
    private lateinit var locationManager: LocationManager

    private final val locationCode=2000
    private final val locationCode1 = 2001

    private lateinit var geofenceHelper: GeofenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val notificationHelper = NotificationHelper(this)
//        notificationHelper.createNotificationChannel()
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted){
//            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
//            startActivity(intent)
//        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)
        var button = findViewById<Button>(R.id.button2)

        val samsung = LatLng(35.205234,126.811794)

        addGeofence(samsung)
        button.setOnClickListener(){
            val notificationHelper = NotificationHelper(this)
            notificationHelper.createNotificationChannel()
            notificationHelper.displayNotification(Random.nextInt(),"button test","test",
                MainActivity().javaClass)
        }
    }

    // ...

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
//        markingOptions = MarkerOptions().position(samsung).title("Marker in Sydney").snippet("good city")
//        mMap.addMarker(markingOptions)
//        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(samsung,12f)
//        mMap.animateCamera(cameraUpdate)
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(samsung))

        getMyLocation()
        // Add more markers and move the camera
    }

    private fun getMyLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(ContextCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled=true
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION),locationCode)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION),locationCode)
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==locationCode){
            if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if((ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)&&(ActivityCompat.checkSelfPermission(
                    this, android.Manifest.permission.ACCESS_COARSE_LOCATION
                )!=PackageManager.PERMISSION_GRANTED)){
                    return
                }
                mMap.isMyLocationEnabled=true
            }

        }
        if(requestCode==locationCode1){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.checkSelfPermission(
                        this, ACCESS_BACKGROUND_LOCATION
                )!=PackageManager.PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)){
                    return
                }
                Toast.makeText(this,"You can add Geofences",Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun addGeofence(p0: LatLng) {
        val geofence = geofenceHelper.getGeofence("ID", Pair(p0.latitude,p0.longitude),100f)
        val geofenceRequest = geofence?.let{geofenceHelper.getGeofencingRequest(it)}
        val pendingIntent = geofenceHelper.geofencePendingIntent
        if(ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return
        }
        geofencingClient.addGeofences(geofenceRequest!!,pendingIntent).run{
            addOnSuccessListener{
                Log.d("Success","Geofence added")
            }
            addOnFailureListener{
                Log.d("Failure","Geofence Not added")
            }

        }
    }

    private fun addCircle(p0: LatLng?) {
        val circleOptions = CircleOptions()
        circleOptions.center(p0)
        circleOptions.radius(100.00)
        circleOptions.strokeColor(255)
        circleOptions.strokeWidth(4f)
        mMap.addCircle(circleOptions)
    }

    private fun addMarker(p0: LatLng){
        markingOptions = MarkerOptions().position(p0)
        mMap.addMarker(markingOptions)
    }
}