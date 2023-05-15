package com.ieumpyo.ieum.map

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableApkTooOldException
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableSdkTooOldException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.ieumpyo.ieum.R
import com.ieumpyo.ieum.map.helpers.ARCoreSessionLifecycleHelper
import com.ieumpyo.ieum.map.helpers.FullScreenHelper
import com.ieumpyo.ieum.map.helpers.GeoPermissionsHelper
import com.ieumpyo.ieum.map.helpers.TrashcanGeoView
import com.ieumpyo.ieum.samplerender.SampleRender
import io.reactivex.disposables.Disposables
import io.reactivex.plugins.RxJavaPlugins

class MapGeoActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MapGeoActivity"
    }

    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
    lateinit var view: TrashcanGeoView
    private lateinit var renderer: MapGeoRenderer
    private var disposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxJavaPlugins.setErrorHandler {
            Log.e("Error", it.localizedMessage ?: "")
        }

        val intent = getIntent()
        val culturalProperty = intent.getStringExtra("culturalProperty")
        Log.d("showGPS",culturalProperty.toString()+"!!")
//    // Setup ARCore session lifecycle helper and configuration.
        arCoreSessionHelper = ARCoreSessionLifecycleHelper(this)
        // If Session creation or Session.resume() fails, display a message and log detailed
        // information.
        arCoreSessionHelper.exceptionCallback =
            { exception ->
                val message =
                    when (exception) {
                        is UnavailableUserDeclinedInstallationException ->
                            resources.getString(R.string.install_google_play)
                        is UnavailableApkTooOldException -> resources.getString(R.string.update_ar_core)
                        is UnavailableSdkTooOldException -> resources.getString(R.string.update_this_app)
                        is UnavailableDeviceNotCompatibleException -> resources.getString(R.string.no_ar_support)
                        is CameraNotAvailableException -> resources.getString(R.string.camera_not_available)
                        else -> resources.getString(R.string.ar_core_exception, exception.toString())
                    }
                Log.e(TAG, "ARCore threw an exception", exception)
                view.snackbarHelper.showError(this, message)
            }

        // Configure session features.
        arCoreSessionHelper.beforeSessionResume = ::configureSession
        lifecycle.addObserver(arCoreSessionHelper)

        // Set up the Trashcan AR renderer.
        renderer = MapGeoRenderer(this, culturalProperty)
        lifecycle.addObserver(renderer)

        // Set up Trashcan AR UI.
        view = TrashcanGeoView(this)
        lifecycle.addObserver(view)
        setContentView(view.root)

        // Sets up an example renderer using our TrashcanGeoRenderer.
        SampleRender(view.surfaceView, renderer, assets)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    // Configure the session, setting the desired options accordding to your usecase.
    private fun configureSession(session: Session) {
        session.configure(
            session.config.apply {
                // Enable Geospatial Mode.
                geospatialMode = Config.GeospatialMode.ENABLED
                // This finding mode is probably the default
                // https://developers.google.com/ar/develop/java/geospatial/terrain-anchors
                planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (!GeoPermissionsHelper.hasGeoPermissions(this)) {
            // Use toast instead of snackbar here since the activity will exit.
            Toast.makeText(this, resources.getString(R.string.permissions_needed), Toast.LENGTH_LONG)
                .show()
            if (!GeoPermissionsHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                GeoPermissionsHelper.launchPermissionSettings(this)
            }
            finish()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }
}
