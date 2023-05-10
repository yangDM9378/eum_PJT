package com.ieumpyo.ieum

import Pin
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.GeolocationPermissions
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
//    var target_url="http://10.0.2.2:3000/"
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
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 1) {
//            if (grantResults.size > 0) {
//                for (i in grantResults.indices) {
//                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                        // 하나라도 거부한다면.
//                        AlertDialog.Builder(this).setTitle("알림").setMessage("권한을 허용해주셔야 앱을 이용할 수 있습니다.")
//                            .setPositiveButton("종료",
//                                DialogInterface.OnClickListener { dialog, which ->
//                                    dialog.dismiss()
//                                    finish()
//                                }).setNegativeButton("권한 설정",
//                                DialogInterface.OnClickListener { dialog, which ->
//                                    dialog.dismiss()
//                                    val intent: Intent =
//                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                            .setData(Uri.parse("package:" + applicationContext.packageName))
//                                    applicationContext.startActivity(intent)
//                                }).setCancelable(false).show()
//                        return
//                    }
//                }
//                //Toast.makeText(this, "Succeed Read/Write external storage !", Toast.LENGTH_SHORT).show();
//                //startApp();
//            }
//        }
//    }


    inner class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun test(){}
    }
    var cameraPath = ""
    var mWebViewImageUpload: ValueCallback<Array<Uri>>? = null

    lateinit var mAdView : AdView
    lateinit var intLst : List<Int>
    lateinit var web : WebView

    companion object{

        var accessToken: MutableLiveData<String> = MutableLiveData()
        lateinit var db: notifiedLocationDB
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        MobileAds.initialize(this) {}
        mAdView = findViewById<AdView>(R.id.adView)
        val adRequest=AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

//        val testDeviceIds = Arrays.asList("ca-app-pub-4728228463704876/6896938382")
        val testDeviceIds = Arrays.asList("ca-app-pub-3940256099942544/6300978111")

        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        checkPermission()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment)as SupportMapFragment
        mapFragment.getMapAsync(this)



//        웹뷰 생성
        web= findViewById<WebView>(R.id.web)
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
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA), 1)
                }
            }

//            override fun onPageFinished(view: WebView, url: String) {
//                super.onPageFinished(view, url);
////               Log.d("Main",token+"!!")
//                accessToken=getCookie(target_url,"accessToken")
//                Log.d("OnPageFinished",accessToken.value.toString()+"!!")
//            }

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
            //카메라 권한
            override fun onPermissionRequest(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.resources)
                }
            }
//           알람창 권한
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override fun onJsConfirm(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                return super.onJsConfirm(view, url, message, result)
            }


            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                try{
                    mWebViewImageUpload = filePathCallback!!
                    val contentSelectionIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    contentSelectionIntent.type = "image/*"

                    val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                    chooserIntent.putExtra(Intent.EXTRA_TITLE,"사용할 앱을 선택해주세요.")
                    launcher.launch(chooserIntent)
                }
                catch (e : Exception){ }
                return true
            }

        }


        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.contentSwipeLayout)
        refreshLayout.setOnRefreshListener { web.reload() }
        refreshLayout.viewTreeObserver.addOnScrollChangedListener {
            if (web.getScrollY() === 0) {
                refreshLayout.isEnabled = true
            } else {
                refreshLayout.isEnabled = false
            }
        }

        accessToken=getCookie(target_url,"accessToken")
        accessToken=MutableLiveData("eyJyZWdEYXRlIjoxNjgzNjc4MDgzMzk4LCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJxb3J3bGRuanMxMDBAbmF2ZXIuY29tIiwidXNlckdlbmRlciI6MSwidXNlckJpcnRoWWVhciI6MTk5NCwidXNlck5hbWUiOiLrsLHsp4Dsm5AiLCJ1c2VySWQiOjIsInN1YiI6InFvcndsZG5qczEwMEBuYXZlci5jb20iLCJleHAiOjE2ODM2OTYwODN9.NNEJCOGQEbmBCcECJFZWwXr1DqTdppeHLY-_GFYnegI")

        accessToken.observe(this){
            initList("Bearer "+it)
            Log.d("Token","ACCESSTOKEN OBSERVE!!"+it)
        }


        db= Room.databaseBuilder(applicationContext, notifiedLocationDB::class.java,"pin").allowMainThreadQueries().fallbackToDestructiveMigration().build()
        db.dao().getAll().observe(this ){ tmp ->
            val StrArr = tmp.map{it.toString()}
            intLst=tmp
            removeGeofence(StrArr)
            Log.d("OBSERVER",tmp.toString()+"!!") }


        getBundle()
        web.loadUrl(target_url) // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        geofencingClient= LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)

    }
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val intent = result.data

            if(intent == null){ //바로 사진을 찍어서 올리는 경우
                val results = arrayOf(Uri.parse(cameraPath))
                mWebViewImageUpload!!.onReceiveValue(results!!)
            }
            else{ //사진 앱을 통해 사진을 가져온 경우
                val results = intent!!.data!!
                mWebViewImageUpload!!.onReceiveValue(arrayOf(results!!))
            }
        }
        else{ //취소 한 경우 초기화
            mWebViewImageUpload!!.onReceiveValue(null)
            mWebViewImageUpload = null
        }
    }

    fun getBundle(){
        Log.d("BUNDLE","getBundle!!!")
        val intent = getIntent()
        val bundle = intent.extras
//        Log.d("OBSERVER!!",bundle?.getInt("pin_id").toString())
        if (bundle != null) {
            if (bundle.getString("url") != null && !bundle.getString("url")
                    .equals("", ignoreCase = true)
            ) {
                target_url = bundle.getString("url")!!

                Log.d("main",target_url+"!!")
            }
            if(bundle.getInt("pin_id")!=null){
                try{
                    db.dao().insert(notifiedLocationEntity(bundle.getInt("pin_id")))
                    Log.d("BUNDLE",bundle.getInt("pin_id").toString()+"!!")

                }
                catch(e:Exception){
                    Log.d("DB",e.toString())
                }
                Log.d("DB",bundle.getInt("pin_id").toString()+"!!")
            }
        }
    }


    fun getCookie(siteName: String, cookieName: String): MutableLiveData<String> {
        var CookieValue: String=""

        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(siteName)
        if(cookies==null){

            return MutableLiveData()
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
                    Log.d("Success",listGeofence.toString()+"!!")
                    listGeofence?.forEach{
                        val isTrue=intLst.binarySearch(it.pinId)
                        if(isTrue<0){
                            val tmp = geofenceHelper.getGeofence(it.pinId.toString(), Pair(it.latitude, it.longitude),500.0f)
                            addGeofence(tmp)
                            Log.d("MAIN",isTrue.toString()+" "+it.toString()+"!!")

                        }

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

        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.ACCESS_NETWORK_STATE
            ) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||  checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //카메라 또는 저장공간 권한 획득 여부 확인
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA
                )
            ) {
                // 카메라 및 저장공간 권한 요청
                requestPermissions(
                    arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 1
                )
            } else { ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.INTERNET, Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)

            }
        }
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

