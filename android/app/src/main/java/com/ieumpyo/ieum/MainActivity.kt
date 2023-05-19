package com.ieumpyo.ieum

import Pin
import Pins
import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
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
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.ERROR
import android.util.Log
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.GeolocationPermissions
import android.webkit.JavascriptInterface
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ieumpyo.ieum.api.RetrofitImpl
import com.ieumpyo.ieum.geofencing.GeofenceHelper
import com.ieumpyo.ieum.map.MapGeoActivity
import com.ieumpyo.ieum.roomdb.notifiedLocationDB
import com.ieumpyo.ieum.roomdb.notifiedLocationEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException
import java.util.Arrays
import java.util.Locale


class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    //    var target_url="http://10.0.2.2:3000/"
       var target_url="https://i-eum-u.com/"

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
            if(shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ){
                requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),locationCode)
            }else{
                requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),locationCode)
            }
            return
        }


        mMap.isMyLocationEnabled=true
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,100.0f,locationListener)

    }

    private var mTTS // TTS 변수 선언
            : TextToSpeech? = null

    inner class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun copyToClipboard(text: String?) {
            val clipboard: ClipboardManager =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("demo", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(mContext, "그룹코드가 복사되었습니다!", Toast.LENGTH_SHORT).show()

        }
        @JavascriptInterface
        fun showGPS(pinId: Int) {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 3)
            }
            val data = setString(pinId)
            Toast.makeText(mContext, "메시지 찾아가기", Toast.LENGTH_SHORT).show()
            val intent = Intent(mContext, MapGeoActivity::class.java)

            intent.putExtra("culturalProperty", data)
            mContext.startActivity(intent)
        }

        @JavascriptInterface
        fun doTTS(msg:String){
            mTTS?.speak(msg,TextToSpeech.QUEUE_FLUSH,null)
        }

        fun reloadList(){
            initList("Bearer "+ accessToken.value.toString())
            Log.d("reloadList","!!!!!!!!!!!!!!!")

        }

    }
    fun setString(id:Int): String{
        lateinit var tmp:String
        val pinList = listGeofence
            pinList?.forEach{
                if(it.pinId==id){
                    tmp = "${it.latitude}|${it.longitude}|${it.pinId}|poi|https://i-eum-u.com/|${it.pinId}"
                    return tmp
                }
            }
        return tmp
    }
    var cameraPath = ""
    var mWebViewImageUpload: ValueCallback<Array<Uri>>? = null

    lateinit var mAdView : AdView
    lateinit var intLst : List<Int>
    lateinit var web : WebView

    companion object {

        var accessToken: MutableLiveData<String> = MutableLiveData()
        lateinit var db: notifiedLocationDB
        var listGeofence: List<Pin>? = null
        var listAll: MutableSet<String>?= mutableSetOf<String>()
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        MobileAds.initialize(this) {}
        mAdView = findViewById<AdView>(R.id.adView)
        val adRequest=AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mTTS = TextToSpeech(this) { status ->
            if (status != ERROR) {
                // 언어를 선택한다.
                mTTS?.setLanguage(Locale.KOREAN)
            }
        }

//        val testDeviceIds = Arrays.asList("ca-app-pub-4728228463704876/6896938382")
        val testDeviceIds = Arrays.asList("ca-app-pub-3940256099942544/6300978111")

        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        checkPermission()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment)as SupportMapFragment
        mapFragment.getMapAsync(this)



//        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.contentSwipeLayout)
//        val fabutton=findViewById<FloatingActionButton>(R.id.floatingActionButton)
//        fabutton.setOnClickListener{
//            web.reload()
//            initList("Bearer "+ accessToken.value.toString())
//
//        }
        lateinit var uri: Uri

        accessToken=getCookie(target_url,"accessToken")
        accessToken.observe(this){
            initList("Bearer "+it)
            Log.d("ACCESS TOKEN","observe!!!!")
        }


//        웹뷰 생성
        web= findViewById<WebView>(R.id.web)
        web.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(true)
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
            settings.databaseEnabled = true
            settings.setGeolocationEnabled(true)
            settings.allowFileAccess = true
            settings.domStorageEnabled=true
            // JavaScript 인터페이스 활성화
            addJavascriptInterface(WebAppInterface(this@MainActivity),"Android")
        }
//        refreshLayout.setOnRefreshListener {
//            web.reload()
//            initList("Bearer "+ accessToken.value.toString())
//            refreshLayout.isRefreshing = true
//        }
//        refreshLayout.viewTreeObserver.addOnScrollChangedListener {
//            if (web.getScrollY() <= 0 ) {
//                refreshLayout.isEnabled = true
//            } else {
//                refreshLayout.isEnabled = false
//            }
//        }

        web.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                checkGPS()
                accessToken=getCookie(target_url,"accessToken")

            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url);
//                refreshLayout.isRefreshing = false
//                Log.d("URL",url.toString()+"!!")
//                if(url?.contains("group")==true|| url?.contains("oauth")==true){
//                    refreshLayout.isEnabled=true
//                }else if(url?.contains("map")==true){
//                    refreshLayout.isEnabled=false
//                }
//                else{
//                    refreshLayout.isEnabled=false
//                }
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)

            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request != null) {
                    if (request.url.scheme == "intent") {
                        try {
                            // Intent 생성
                            val intent =
                                Intent.parseUri(request.url.toString(), Intent.URI_INTENT_SCHEME)

                            // 카카오톡이 깔려 있으면 그대로 코드 실행, 깔려 있지 않으면 예외 발생
                            packageManager.getPackageInfo(
                                "com.kakao.talk",
                                PackageManager.GET_ACTIVITIES
                            )

                            startActivity(intent)
                            return true
                        } catch (e: URISyntaxException) {
                            Log.e("kakaoTalk", "Invalid intent request", e)
                        } catch (e: Exception) {
                            // 실행 못하면 웹뷰는 에러가 발생하므로
                            // 본인이 원하는 사이트로 이동(보통 메인으로 redirect)
                            web.loadUrl("http://i-eum-u.com/")

                            // 플레이 스토어 - 카카오톡으로 이동
                            val intentStore = Intent(Intent.ACTION_VIEW)
                            intentStore.addCategory(Intent.CATEGORY_DEFAULT)
                            intentStore.data = Uri.parse("market://details?id=com.kakao.talk")
                            startActivity(intentStore)
                        }
                    }
                    uri=Uri.parse(request.url.toString())
                }
                return false
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
                initList("Bearer "+accessToken.value.toString())
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

            override fun onJsPrompt(
                view: WebView?,
                url: String?,
                message: String?,
                defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                return super.onJsPrompt(view, url, message, defaultValue, result)
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


        db= Room.databaseBuilder(applicationContext, notifiedLocationDB::class.java,"pin").allowMainThreadQueries().fallbackToDestructiveMigration().build()
        db.dao().getAll().observe(this ){ tmp ->
            val StrArr = tmp.map{it.toString()}
            Log.d("API",StrArr.toString()+"!!")
            intLst=tmp
            removeGeofence(StrArr) }


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
        val intent = getIntent()
        val bundle = intent.extras
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


                }
                catch(e:Exception){
                    Log.d("DB",e.toString())
                }
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
        Log.d("initList",token+"!!")
        RetrofitImpl.service.getPinAll(token).enqueue(object : Callback<Pins>{
            override fun onFailure(call: Call<Pins>, t: Throwable) {
                Log.e("Failed",t.toString()+"!!")
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onResponse(
                call: Call<Pins>,
                response: Response<Pins>
            ) {
                if(response.isSuccessful){
                    val rawlist = response.body()
                    if (rawlist != null) {
                        listGeofence = rawlist.result
                    }
                    listGeofence?.forEach{
                        listAll?.add("${it.latitude}|${it.longitude}|${it.pinId}|poi|https://i-eum-u.com/|${it.pinId}")
                        val isTrue=intLst.binarySearch(it.pinId)
                        if(isTrue<0){
                            val tmp = geofenceHelper.getGeofence(it.pinId.toString(), Pair(it.latitude, it.longitude),100.0f)
                            addGeofence(tmp)
                        }

                    }

                }else{
                    Log.d("Response errorBody", response.errorBody()?.string()+"!!");

                }
            }

        })

    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun addGeofence(geofence : Geofence) {
        Log.d("Geopfence",geofence.toString()+"!!")
        val geofenceRequest = geofence?.let{geofenceHelper.getGeofencingRequest(it)}
        val pendingIntent = geofenceHelper.geofencePendingIntent

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        geofencingClient.addGeofences(geofenceRequest!!,pendingIntent).run{
            addOnSuccessListener{
                Log.d("Success","Geofence added!!")
            }
            addOnFailureListener{
                e->
                Log.d("Failure","Geofence Not added!!", e)
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


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermission() {

        if (checkSelfPermission(Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE
            ) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //카메라 또는 저장공간 권한 획득 여부 확인
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS))
            ) {
                // 카메라 및 저장공간 권한 요청
                requestPermissions(
                    arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.POST_NOTIFICATIONS
                    ), 1
                )
            } else { requestPermissions(this,
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.POST_NOTIFICATIONS
                                ), 1)

            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkGPS(){
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck == PackageManager.PERMISSION_DENIED) { //포그라운드 위치 권한 확인
            //위치 권한 요청
            requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }


        val permissionCheck2 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        if (permissionCheck2 == PackageManager.PERMISSION_DENIED) { //백그라운드 위치 권한 확인
            //위치 권한 요청
            requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                0
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
        getBundle()
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

