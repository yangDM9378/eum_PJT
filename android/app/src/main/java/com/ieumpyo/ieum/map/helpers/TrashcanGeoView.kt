/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ieumpyo.ieum.map.helpers

import android.opengl.GLSurfaceView
import android.view.View
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.maps.SupportMapFragment
import com.google.ar.core.Earth
import com.google.ar.core.GeospatialPose
import com.ieumpyo.ieum.R
import com.ieumpyo.ieum.map.MapGeoActivity

/** Contains UI elements for Trashcan Geo. */
class TrashcanGeoView(val activity: MapGeoActivity) : DefaultLifecycleObserver {
  val root: View = View.inflate(activity, R.layout.activity_gps, null)
  val surfaceView: GLSurfaceView = root.findViewById(R.id.surfaceview)

  val session
    get() = activity.arCoreSessionHelper.session

  val snackbarHelper = SnackbarHelper()

  var mapView: MapView? = null
//  val mapTouchWrapper: MapTouchWrapper = root.findViewById<MapTouchWrapper>(R.id.map_wrapper).apply {
//    setup {
//      activity.renderer.onMapClick()
//    }
//  }
  val mapFragment =
    (activity.supportFragmentManager.findFragmentById(R.id.map)!! as SupportMapFragment).also {
      it.getMapAsync { googleMap -> mapView = MapView(activity, googleMap) }
    }

  private val statusText: TextView = root.findViewById(R.id.statusText)
  fun updateStatusText(earth: Earth, cameraGeospatialPose: GeospatialPose?) {
    activity.runOnUiThread {
      val poseText = if (cameraGeospatialPose == null) "" else
      activity.getString(R.string.geospatial_pose,
                         cameraGeospatialPose.latitude,
                         cameraGeospatialPose.longitude,
                         cameraGeospatialPose.horizontalAccuracy,
                         cameraGeospatialPose.altitude,
                         cameraGeospatialPose.verticalAccuracy,
                         cameraGeospatialPose.heading,
                         cameraGeospatialPose.headingAccuracy
      )
      val statusMessage = activity.resources.getString(
        R.string.earth_state,
        earth.earthState.toString(),
        earth.trackingState.toString(),
        poseText
      )
      statusText.text = statusMessage
    }
  }

  fun updateStatusTextString(statusMessage: String) {
    activity.runOnUiThread {
      if (!statusText.text.equals(statusMessage)) {
        statusText.text = statusMessage
      }
    }
  }

  override fun onResume(owner: LifecycleOwner) {
    surfaceView.onResume()
  }

  override fun onPause(owner: LifecycleOwner) {
    surfaceView.onPause()
  }
}
