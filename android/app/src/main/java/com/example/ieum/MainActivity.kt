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
        var button = findViewById<Button>(R.id.button)

        button.setOnClickListener(){
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
//            displayNotification()
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
//        geofencingClient= LocationServices.getGeofencingClient(this)
//        addGeofences()
    }
//    val geofenceList: MutableList<Geofence> by lazy {
//        mutableListOf(
//            getGeofence("현대백화점", Pair(37.5085864,127.0601149),100f),
//            getGeofence("삼성역", Pair(37.5094518,127.063603),100f),
//            getGeofence("광주삼성", Pair(35.205234,126.811794),100f)
//        )
//    }


//    private fun addGeofences(){
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_LONG).show()
//            checkPermission()
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)
//            .addOnSuccessListener(this,
//                OnSuccessListener<Void?> {
//                                    Toast.makeText(this@MainActivity, "Geofencing has started", Toast.LENGTH_SHORT).show()
//
//                    Log.d(TAG, "onSuccess: geofenccess added!") })
//            .addOnFailureListener(this,
//                OnFailureListener { e ->
//                    Log.d(
//                        TAG,
//                        "Geofencing failed : " + e.message
//                    )
//                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
//                })
////        geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
////            addOnSuccessListener {
////                Toast.makeText(this@MainActivity, "add Success", Toast.LENGTH_LONG).show()
////            }
////
////            addOnFailureListener{
////
////                Toast.makeText(this@MainActivity, "add Fail", Toast.LENGTH_LONG).show()
////            }
////        }
//    }


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


    companion object {
        val mContext = this
    }
}

