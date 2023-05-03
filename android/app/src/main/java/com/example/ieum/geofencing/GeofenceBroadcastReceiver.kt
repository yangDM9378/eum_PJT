package com.example.ieum.geofencing

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.ieum.MainActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import kotlin.random.Random


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
        val geofenceId = geofencingEvent?.triggeringGeofences?.get(0)?.requestId
        // Get the transition type.
        val transitionTypes = geofencingEvent?.geofenceTransition

//        geofencelist = geofencingEvent?.triggeringGeofences as List<Geofence>
        when (transitionTypes) {

            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                notificationHelper.displayNotification(
                    Random.nextInt(),
                    geofenceId!!,
                    "ENTER TEST BODY TEXT",
                    MainActivity().javaClass
                )
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                notificationHelper.displayNotification(
                    Random.nextInt(),
                    geofenceId!!,
                    "DWELL TEST BODY TEXT",
                    MainActivity().javaClass
                )
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                notificationHelper.displayNotification(
                    Random.nextInt(),
                    geofenceId!!,
                    "EXIT TEST BODY TEXT",
                    MainActivity().javaClass
                )
            }
        }


    }

}