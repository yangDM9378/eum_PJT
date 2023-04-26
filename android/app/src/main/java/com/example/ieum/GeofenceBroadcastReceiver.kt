package com.example.ieum

import android.R
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    // ...

    override fun onReceive(context: Context?, intent: Intent) {
        Log.d(TAG, "onReceive")
        Toast.makeText(context,"온 리시브",Toast.LENGTH_SHORT).show()

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
                Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT)
                Log.e(TAG, "ERROR: "+errorMessage)
                return
            }
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent?.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
        geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            // Get the transition details as a String.
//            val geofenceTransitionDetails = getGeofenceTransitionDetails(
//                this,
//                geofenceTransition,
//                triggeringGeofences
//            )
            (MainActivity.mContext as MainActivity).displayNotification()

            // Send notification and log the transition details.
//            sendNotification(geofenceTransitionDetails)
            val transitionMsg = when(geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> "Enter"
                Geofence.GEOFENCE_TRANSITION_EXIT -> "Exit"
                else -> "-"
            }
            if (triggeringGeofences != null) {
                triggeringGeofences.forEach {
                    Toast.makeText(context, "${it.requestId} - $transitionMsg", Toast.LENGTH_LONG).show()
                sendNotification(it.requestId, MainActivity.mContext)
                }
            }

        } else {
            Toast.makeText(context, "Unknown", Toast.LENGTH_LONG).show()
        }
    }
    private fun sendNotification(locId: String, context: Context) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "CHANNEL01")
            .setSmallIcon(R.drawable.alert_dark_frame)
            .setContentTitle("Location Reached")
            .setContentText(" you reached $locId")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set the intent that will fire when the user taps the notification
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build())
    }

}