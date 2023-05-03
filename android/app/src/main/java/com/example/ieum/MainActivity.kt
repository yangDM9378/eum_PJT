package com.example.ieum

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ieum.api.Result
import com.example.ieum.api.RetrofitImpl
import com.example.ieum.geofencing.GeofenceBroadcastReceiver
import com.example.ieum.geofencing.GeofenceHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import retrofit2.Call
import retrofit2.Response
import java.util.Arrays


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val KEY_REPLY="key_reply"

   var target_url="http://i-eum-u.com/"
    private val MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION = 100
    private val MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION = 101

    private lateinit var  geofencingClient: GeofencingClient
    private lateinit var geofenceHelper: GeofenceHelper

    private lateinit var locationManager: LocationManager
    var locationProvider : String? = null

    val locationListener = object:LocationListener{
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            super.onStatusChanged(provider, status, extras)
        }
        override fun onLocationChanged(location: Location) {
            val longitude = location.longitude
            val latitude = location.latitude

            Log.d("Location",longitude.toString()+" "+latitude.toString())
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
    }

    private lateinit var mMap : GoogleMap

    val geofenceList: MutableList<Geofence> by lazy {
        mutableListOf(
            geofenceHelper.getGeofence("현대", Pair(37.5085864,127.0601149),100f),
            geofenceHelper.getGeofence("삼성", Pair(37.5094518,127.063603),100f),
            geofenceHelper.getGeofence("삼성광주", Pair(35.205234,126.811794),100f)
        )
    }

    private final val locationCode=2000
    private final val locationCode1 = 2001

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        getMyLocation()
        addGeofence(geofenceList)
    }
    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),locationCode)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),locationCode)
            }
            return
        }


        mMap.isMyLocationEnabled=true
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,100.0f,locationListener)
        Log.d("Main","${location?.latitude} ${location?.longitude}!!")

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==locationCode){
            if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if((ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )!=PackageManager.PERMISSION_GRANTED)&&(ActivityCompat.checkSelfPermission(
                        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )!=PackageManager.PERMISSION_GRANTED)){
                    return
                }
            }

        }
        if(requestCode==locationCode1){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )!=PackageManager.PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)){
                    return
                }
                Toast.makeText(this,"You can add Geofences", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun test(){}
    }

    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}
        mAdView = findViewById<AdView>(R.id.adView)
        val adRequest=AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val testDeviceIds = Arrays.asList("ca-app-pub-3940256099942544/6300978111")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        checkPermission()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment)as SupportMapFragment
        mapFragment.getMapAsync(this)

//        웹뷰 생성
        var web= findViewById<WebView>(R.id.web)
        web.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(true)
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
            settings.databaseEnabled = true
            settings.setGeolocationEnabled(true)
            settings.allowFileAccess = true
            settings.domStorageEnabled=true
            // JavaScript 인터페이스 활성화
            addJavascriptInterface(WebAppInterface(this@MainActivity),"WebAppInterface")
        }

        val intent = intent
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.getString("url") != null && !bundle.getString("url")
                    .equals("", ignoreCase = true)
            ) {
                target_url = bundle.getString("url")!!

                Log.d("main",target_url+"!!")
            }
        }

        web.loadUrl(target_url) // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        val token="Bearer%20eyJyZWdEYXRlIjoxNjgzMDg5ODI4MzcyLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyR2VuZGVyIjoxLCJ1c2VyQmlydGhZZWFyIjoxOTk0LCJ1c2VyTmFtZSI6IuuwseyngOybkCIsInVzZXJJZCI6InFvcndsZG5qczEwMEBuYXZlci5jb20iLCJzdWIiOiJxb3J3bGRuanMxMDBAbmF2ZXIuY29tIiwiZXhwIjoxNjgzMTA3ODI4fQ.U2Q390nsuDlfpLJLlW8-sKMi2hvzAHvvC8vi7EWryaY"

        geofencingClient= LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)

        Log.d(ContentValues.TAG, geofenceList.toString()+"!!")
        initList(token)

        val serviceIntent =
            Intent(this, GeofenceBroadcastReceiver::class.java) // MyBackgroundService 를 실행하는 인텐트 생성

        startService(serviceIntent) // 서비스 인텐트를 전달한 서비스 시작 메서드 실행





    }
    private fun initList(token: String){
        val pin = RetrofitImpl.service.getPinAll(token).enqueue(object : retrofit2.Callback<List<Result.Pin>>{
            override fun onFailure(call: Call<List<Result.Pin>>, t: Throwable) {
                Log.e("Failed",t.toString()+"!!")
            }

            override fun onResponse(
                call: Call<List<Result.Pin>>,
                response: Response<List<Result.Pin>>
            ) {
                if(response.isSuccessful){

                    val listGeofence = response.body()
                    Log.d("Success",listGeofence.toString()+"!!")
                    listGeofence?.forEach{
                            it->geofenceHelper.getGeofence(it.pin_id.toString(), Pair(it.latitude, it.longitude),it.radius)
                        Log.d("!!",it.toString())
                    }
                }
            }
        })
        RetrofitImpl.service.getGroupAll(token,3).enqueue(object : retrofit2.Callback<Result.Response>{

            override fun onFailure(call: Call<Result.Response>, t: Throwable) {

                Log.e("Failed",t.toString()+"!!!!!")
            }

            override fun onResponse(call: Call<Result.Response>, response: Response<Result.Response>) {
                if(response.isSuccessful){
                    val rawList = response.body()
                    Log.d("Success!!",rawList.toString())
                }
            }
        })
    }


    private fun addGeofence(geofences : List<Geofence>) {
        val geofenceRequest = geofences?.let{geofenceHelper.getGeofencingRequest(it)}

        val pendingIntent = geofenceHelper.geofencePendingIntent
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return
        }
        geofencingClient.addGeofences(geofenceRequest!!,pendingIntent).run{
            addOnSuccessListener{
                Log.d("Success","Geofence added!!")
            }
            addOnFailureListener{
                Log.d("Failure","Geofence Not added!!")
            }

        }
    }

    private fun removeGeofence(geofencesId: List<String>){
        geofencingClient.removeGeofences(geofencesId).run {
            addOnSuccessListener {
                Log.d("Success","Geofence removed")
            }
            addOnFailureListener {
                Log.d("Failure","Geofence Not Removed")
            }
        }
    }


    private fun checkPermission() {
        val permissionAccessFineLocationApproved = ActivityCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (permissionAccessFineLocationApproved) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val backgroundLocationPermissionApproved = ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED

                if (!backgroundLocationPermissionApproved) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION
                    )
                }
            }
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION
            )
        }
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        target_url="http://i-eum-u.com/"
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}

