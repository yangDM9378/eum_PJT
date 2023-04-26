package com.example.ieum

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener


class MainActivity : ComponentActivity() {
    private val CHANNEL_ID = "com.example.ieum"
    private var notificationManager: NotificationManager? = null
    private val KEY_REPLY="key_reply"

    private val MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION = 100
    private val MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION = 101

    private lateinit var  geofencingClient: GeofencingClient
    private lateinit var locationManager: LocationManager
    inner class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun test(){}
    }

    var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(CHANNEL_ID,"메시지 알람","this is a test channel")
        var button = findViewById<Button>(R.id.button)

        button.setOnClickListener(){
            displayNotification()
        }
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
            // JavaScript 인터페이스 활성화
            addJavascriptInterface(WebAppInterface(this@MainActivity),"WebAppInterface")
        }
        web.loadUrl("https://www.naver.com/")
        geofencingClient= LocationServices.getGeofencingClient(this)
        addGeofences()
    }
    val geofenceList: MutableList<Geofence> by lazy {
        mutableListOf(
            getGeofence("현대백화점", Pair(37.5085864,127.0601149),100f),
            getGeofence("삼성역", Pair(37.5094518,127.063603),100f),
            getGeofence("광주삼성", Pair(35.205234,126.811794),100f)
        )
    }
    private fun getGeofence(reqId: String, geo: Pair<Double,Double>,radius:Float=100f): Geofence {
        return Geofence.Builder()
            .setRequestId(reqId)    // 이벤트 발생시 BroadcastReceiver에서 구분할 id
            .setCircularRegion(geo.first, geo.second, radius)    // 위치 및 반경(m)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)        // Geofence 만료 시간
            .setLoiteringDelay(10000)                            // 머물기 체크 시간
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER                // 진입 감지시
                        or Geofence.GEOFENCE_TRANSITION_EXIT    // 이탈 감지시
                        or Geofence.GEOFENCE_TRANSITION_DWELL)    // 머물기 감지시
            .build()
    }
    private fun getGeofencingRequest(): GeofencingRequest {
        Toast.makeText(this,"getGeofencingRequest",Toast.LENGTH_SHORT).show()
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this,GeofenceBroadcastReceiver::class.java)
        if(Build.VERSION.SDK_INT > 30){
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        } else{
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private fun addGeofences(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_LONG).show()
            checkPermission()
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)
            .addOnSuccessListener(this,
                OnSuccessListener<Void?> {
                                    Toast.makeText(this@MainActivity, "Geofencing has started", Toast.LENGTH_SHORT).show()

                    Log.d(TAG, "onSuccess: geofenccess added!") })
            .addOnFailureListener(this,
                OnFailureListener { e ->
                    Log.d(
                        TAG,
                        "Geofencing failed : " + e.message
                    )
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                })
//        geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
//            addOnSuccessListener {
//                Toast.makeText(this@MainActivity, "add Success", Toast.LENGTH_LONG).show()
//            }
//
//            addOnFailureListener{
//
//                Toast.makeText(this@MainActivity, "add Fail", Toast.LENGTH_LONG).show()
//            }
//        }
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
     fun displayNotification(){
         Log.d(TAG, "display notification")

         //알람 콘텐츠 설정
        val pinId=45
        val notificationId = pinId
        val tapResultIntent = Intent(this, SecondActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,tapResultIntent,PendingIntent.FLAG_IMMUTABLE)


        val notification: Notification = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
            .setContentTitle("Demo Title") // 노티 제목
            .setContentText("This is a demo notification") // 노티 내용
            .setSmallIcon(android.R.drawable.ic_dialog_info) //아이콘이미지
            .setAutoCancel(true) // 사용자가 알림을 탭하면 자동으로 알림을 삭제합니다.
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) //노티클릭시 인텐트작업
            .build()
        /* 3. 알림 표시*///---------------------------------------------------------------------------
        //NotificationManagerCompat.notify()에 전달하는 알림 ID를 저장해야 합니다.
        // 알림을 업데이트하거나 삭제하려면 나중에 필요하기 때문입니다.
        notificationManager?.notify(notificationId, notification) //노티실행
    }
    private fun createNotificationChannel(id:String, name: String, channelDescription: String){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply{
                description=channelDescription

            }
            notificationManager?.createNotificationChannel(channel)
        }else{

        }
    }

    companion object {
        val mContext = this
    }
}

