package com.ieumpyo.ieum

import Pin
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.GeolocationPermissions
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
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
import com.ieumpyo.ieum.api.RetrofitImpl
import com.ieumpyo.ieum.geofencing.GeofenceHelper
import com.ieumpyo.ieum.roomdb.notifiedLocationDB
import com.ieumpyo.ieum.roomdb.notifiedLocationEntity
import retrofit2.Call
import retrofit2.Response
import java.util.Arrays


class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    private var NEXT_PUBLIC_OUATH_KAKAO_HOSTNAME="http://i-eum-u.com/api/v1/oauth2/authorize/kakao"
    private var NEXT_PUBLIC_OUATH_KAKAO_REDIRECT_URL="http://i-eum-u.com/api/v1/oauth2/callback/kakao"
//    var target_url="http://10.0.2.2:8080"
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


    private final val locationCode=2000
    private final val locationCode1 = 2001

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        getMyLocation()
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
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode==locationCode){
//            if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                if((ActivityCompat.checkSelfPermission(this,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    )!=PackageManager.PERMISSION_GRANTED)&&(ActivityCompat.checkSelfPermission(
//                        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
//                    )!=PackageManager.PERMISSION_GRANTED)){
//                    return
//                }
//            }
//
//        }
//        if(requestCode==locationCode1){
//            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                if(ActivityCompat.checkSelfPermission(
//                        this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                    )!=PackageManager.PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)){
//                    return
//                }
//                Toast.makeText(this,"You can add Geofences", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    inner class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun test(){}
    }

    lateinit var mAdView : AdView

    companion object{

        var accessToken: MutableLiveData<String> = MutableLiveData()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        MobileAds.initialize(this) {}
        mAdView = findViewById<AdView>(R.id.adView)
        val adRequest=AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val testDeviceIds = Arrays.asList("ca-app-pub-4728228463704876/6896938382")
//        val testDeviceIds = Arrays.asList("ca-app-pub-3940256099942544/6300978111")

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

        accessToken=getCookie(target_url,"accessToken")
        accessToken.observe(this){
            initList("Bearer "+it)
//            Log.d("Token","ACCESSTOKEN OBSERVE!!"+it)
        }

        web.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url);
//               Log.d("Main",token+"!!")
                accessToken=getCookie(target_url,"accessToken")
//                Log.d("OnPageFinished",accessToken.value.toString()+"!!")
            }

        })
        web.webChromeClient= WebChromeClient()
        web.webChromeClient=object: WebChromeClient(){
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                super.onGeolocationPermissionsShowPrompt(origin, callback)
                callback?.invoke(origin,true,false)
            }

        }
        val db= Room.databaseBuilder(applicationContext, notifiedLocationDB::class.java,"pin").allowMainThreadQueries().build()
        db.dao().getAll().observe(this ){ tmp ->
            val StrArr = tmp.map{it.toString()}
            removeGeofence(StrArr)

            Log.d("OBSERVER",tmp.toString()+"!!") }

        val intent = intent
        val bundle = intent.extras
//        Log.d("OBSERVER!!",bundle?.getInt("pin_id").toString())
        if (bundle != null) {
            if (bundle.getString("url") != null && !bundle.getString("url")
                    .equals("", ignoreCase = true)
            ) {
                target_url = bundle.getString("url")!!

//                Log.d("main",target_url+"!!")
            }
            if(bundle.getInt("pin_id")!=null){
                db.dao().insert(notifiedLocationEntity(bundle.getInt("pin_id")))
            }
        }

        web.loadUrl(target_url) // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        geofencingClient= LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)

    }
    fun getCookie(siteName: String, cookieName: String): MutableLiveData<String> {
        var CookieValue: String=""

        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(siteName)
        if(cookies==null){

            return MutableLiveData(CookieValue)
        }

//        Log.d("ACCESSTOKEN","cookie : "+cookies+"!!")
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
        return MutableLiveData(CookieValue)
    }
    private fun initList(token: String){

        val db= Room.databaseBuilder(applicationContext, notifiedLocationDB::class.java,"pin").allowMainThreadQueries().build()
        val notifiedList=db.dao().getAll()
        Log.d("Geofence","InitLIST START!!"+token)
        RetrofitImpl.service.getPinAll(token).enqueue(object : retrofit2.Callback<Pin>{
            override fun onFailure(call: Call<Pin>, t: Throwable) {
                Log.e("Failed",t.toString()+"!!")
            }

            override fun onResponse(
                call: Call<Pin>,
                response: Response<Pin>
            ) {
                if(response.isSuccessful){

                    val rawlist = response.body()
                    val listGeofence = rawlist?.result
//                    Log.d("Success",listGeofence.toString()+"!!")
                    listGeofence?.forEach{
                        val tmp = geofenceHelper.getGeofence(it.pinId.toString(), Pair(it.latitude, it.longitude),500.0f)

                        addGeofence(tmp)
//                        Log.d("MAIN",it.toString()+"!!")

                    }

                }else{
                    Log.d("Response errorBody", response.errorBody()?.string()+"!!");

                }
            }

        })

    }


    private fun addGeofence(geofence : Geofence) {
        val geofenceRequest = geofence?.let{geofenceHelper.getGeofencingRequest(it)}
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
    private var backBtnTime: Long = 0

    override fun onBackPressed() {
        val curTime = System.currentTimeMillis()
        val gapTime = curTime - backBtnTime
        val web = findViewById<WebView>(R.id.web)
        if (web.canGoBack()) {
            web.goBack()
        } else if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed()
        } else {
            backBtnTime = curTime
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
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

