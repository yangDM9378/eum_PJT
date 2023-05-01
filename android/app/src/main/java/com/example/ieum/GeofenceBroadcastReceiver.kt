package com.example.ieum

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
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

        // Get the transition type.
        val transitionTypes = geofencingEvent?.geofenceTransition

//        geofencelist = geofencingEvent?.triggeringGeofences as List<Geofence>
        when (transitionTypes) {

            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                notificationHelper.displayNotification(
                    Random.nextInt(),
                    "ENTER",
                    "TEST BODY TEXT",
                    MainActivity().javaClass
                )
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                notificationHelper.displayNotification(
                    Random.nextInt(),
                    "DWELL",
                    "TEST BODY TEXT",
                    MainActivity().javaClass
                )
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                notificationHelper.displayNotification(
                    Random.nextInt(),
                    "EXIT",
                    "TEST BODY TEXT",
                    MainActivity().javaClass
                )
            }
        }


    }

}