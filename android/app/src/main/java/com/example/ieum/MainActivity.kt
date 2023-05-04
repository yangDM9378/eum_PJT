package com.example.ieum

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.ieum.api.Result
import com.example.ieum.api.RetrofitImpl
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
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.Arrays


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var token : String

    private var NEXT_PUBLIC_OUATH_KAKAO_HOSTNAME="http://i-eum-u.com/api/v1/oauth2/authorize/kakao"
    private var NEXT_PUBLIC_OUATH_KAKAO_REDIRECT_URL="http://i-eum-u.com/api/v1/oauth2/callback/kakao"
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
        web.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url);
//                Log.d("Main",token+"!!")
                token=getCookie(target_url,"accessToken")
                initList("Bearer "+token)
            }

//            override fun onLoadResource(view: WebView?, url: String?) {
//                super.onLoadResource(view, url)
//                val targetUrl = (NEXT_PUBLIC_OUATH_KAKAO_HOSTNAME  +
//                        "?redirect_url=" + NEXT_PUBLIC_OUATH_KAKAO_REDIRECT_URL).toRegex()
//                Log.d("MAIN",targetUrl.toString() + "          "+url!!.toRegex()+"!!")
//
//                Log.d("MAIN",targetUrl.matches(url).toString()+"!!")
//                if(targetUrl.matches(url)){
//
//                }
//            }
        })
        val tokenObserver = Observer<String>{
            token->initList(token)
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
        val cookieManager = CookieManager.getInstance().getCookie(target_url)
        Log.d("MAIN", cookieManager+"!!")

        geofencingClient= LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)

        Log.d(ContentValues.TAG, geofenceList.toString()+"!!")


    }
    fun getCookie(siteName: String, cookieName: String): String {
        var CookieValue: String = ""
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(siteName)
        val temp = cookies.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (ar1 in temp) {
            if (ar1.contains(cookieName!!)) {
                val temp1 = ar1.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                CookieValue = temp1[1]
                break
            }
        }
        return CookieValue
    }
    private fun initList(token: String){
        Log.d("MAIN","INIT LIST START!!")

        RetrofitImpl.service.getPinAll(token).enqueue(object : retrofit2.Callback<List<Result.Pin>>{
            override fun onFailure(call: Call<List<Result.Pin>>, t: Throwable) {
                Log.e("Failed",t.toString()+"!!")
                Log.d("MAIN","INIT LIST END!!")

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
                        Log.d("MAIN",it.toString()+"!!")

                    }
                    Log.d("MAIN","INIT LIST END!!")

                }else{
                    Log.d("Response errorBody", response.errorBody()?.string()+"!!");

                }
            }

        })
        RetrofitImpl.service.getGroupAll(token,1).enqueue(object : retrofit2.Callback<Result.ResponseGroup>{

            override fun onFailure(call: Call<Result.ResponseGroup>, t: Throwable) {

                Log.e("Failed",t.toString()+"!!!!!")
            }

            override fun onResponse(call: Call<Result.ResponseGroup>, response: Response<Result.ResponseGroup>) {
                Log.d("Main","response!!"+response)
                if(response.isSuccessful){
                    val rawList = response.body()
                    val js=JSONObject(Gson().toJson(rawList)).getString("result")
                    val name=JSONObject(js).getString("name")

                    Log.d("Success!!",js+name+"!!")
                    Log.d("MAIN","INIT LIST END!!")

                }else  {
                    Log.d("Response errorBody", response.errorBody()?.string()+"!!");
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
        CookieSyncManager.getInstance().startSync()
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

