package com.ieumpyo.ieum.geofencing

import Detail
import PinDetail
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ieumpyo.ieum.MainActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.ieumpyo.ieum.api.RetrofitImpl
import retrofit2.Call
import retrofit2.Response


class GeofenceBroadcastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive!!")
        val notificationHelper = NotificationHelper(context)

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
                Log.e("ERROR", errorMessage)
            }
        }

        // Get the geofences that were triggered. A single event can trigger
        // multiple geofences.
        val location = geofencingEvent?.triggeringLocation
        val geofenceId = geofencingEvent?.triggeringGeofences?.get(0)?.requestId?.toInt()

        // Get the transition type.
        val transitionTypes = geofencingEvent?.geofenceTransition

        lateinit var rawlist : PinDetail

        RetrofitImpl.service.getPinDetail("Bearer "+MainActivity.accessToken.value!!,geofenceId!!).enqueue(object : retrofit2.Callback<PinDetail>{
            override fun onFailure(call: Call<PinDetail>, t: Throwable) {
                Log.e("Failed",t.toString()+"!!")
            }

            override fun onResponse(
                call: Call<PinDetail>,
                response: Response<PinDetail>
            ) {
                if(response.isSuccessful){
                    rawlist = response.body()!!
                    Log.d("API",rawlist.toString())
                    val title=rawlist.result.title
                    val role = rawlist.result.role
                    when (transitionTypes) {

                        Geofence.GEOFENCE_TRANSITION_ENTER -> {
                            notificationHelper.displayNotification(
                                geofenceId!!,
                                "${title}",
                                "${role}님이 남긴 메시지에 접근했어요!",
                                MainActivity().javaClass
                            )
                        }

                        Geofence.GEOFENCE_TRANSITION_DWELL -> {
                            notificationHelper.displayNotification(
                                geofenceId!!,
                                "${title}",
                                "${role}님이 남긴 메시지에 접근했어요!",
                                MainActivity().javaClass
                            )
                        }

                        Geofence.GEOFENCE_TRANSITION_EXIT -> {
                            notificationHelper.displayNotification(
                                geofenceId!!,
                                "${title}",
                                "${role}님이 남긴 메시지에서 멀어졌어요!",
                                MainActivity().javaClass
                            )
                        }
                    }

                }else{
                    Log.d("Response errorBody", response.errorBody()?.string()+"!!");
                }
            }

        })

//        geofencelist = geofencingEvent?.triggeringGeofences as List<Geofence>



    }

}
