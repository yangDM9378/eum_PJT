package com.example.ieum

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest

class GeofenceHelper(context: Context?): ContextWrapper(context) {
    private final val TAG: String = "GEOFENCEHELPER"
    fun getGeofence(reqId: String, geo: Pair<Double,Double>,radius:Float=100f): Geofence {
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
    fun getGeofencingRequest(geofence : Geofence): GeofencingRequest {
        Toast.makeText(this,"getGeofencingRequest", Toast.LENGTH_SHORT).show()
        return GeofencingRequest.Builder().apply {
            addGeofence(geofence)
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)

        }.build()
    }
    val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this,GeofenceBroadcastReceiver::class.java)
        if(Build.VERSION.SDK_INT > 30){
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        } else{
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
    fun getErrorString(e: Exception): String{
        if(e is ApiException){
            val apiException = e
            when(apiException.statusCode){
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> return "Geofence Not Available"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> return "Too many Geofence to update"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> return "Too many pending geofences"
            }
        }
        return e.localizedMessage
    }

}