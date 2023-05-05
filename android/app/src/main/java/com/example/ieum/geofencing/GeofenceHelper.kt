package com.example.ieum.geofencing

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest

class GeofenceHelper(context: Context?): ContextWrapper(context) {
    private final val TAG: String = "GEOFENCEHELPER"

//    지오펜싱 객체 생성, 지오펜싱의 반경과 지속시간, 전환 유형 설정
    fun getGeofence(reqId: String, geo: Pair<Double,Double>,radius:Float=100f): Geofence {
        return Geofence.Builder()
            .setRequestId(reqId)    // 이벤트 발생시 BroadcastReceiver에서 구분할 id
            .setCircularRegion(geo.first, geo.second, radius)    // 위치 및 반경(m)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)        // Geofence 만료 시간
            .setLoiteringDelay(10000)                            // 머물기 체크 시간(ms)
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER                // 진입 감지시
                        or Geofence.GEOFENCE_TRANSITION_EXIT    // 이탈 감지시
                        or Geofence.GEOFENCE_TRANSITION_DWELL)    // 머물기 감지시
            .build()
    }


//    모니터링 할 지오펜싱 지정 및 이벤트 트리거 방식 설정
    fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
//        모니터링할 지오펜스 추가
            addGeofence(geofence)
//            기기가 이미 지오펜싱 내에 있는 경우 ENTER 트리거, 나갈때 EXIT 트리거로 전환
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        }.build()
    }

//    지오펜싱 전환 이벤트 캐치를 위한 broadcast receiver 정의
    val geofencePendingIntent: PendingIntent by lazy {
    Log.d(ContentValues.TAG, "start geofencePendingIntent!!")

    val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
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