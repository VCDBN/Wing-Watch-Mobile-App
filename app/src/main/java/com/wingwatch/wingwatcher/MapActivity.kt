package com.wingwatch.wingwatcher

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.wingwatch.wingwatcher.GlobalVariables.coords
import com.wingwatch.wingwatcher.GlobalVariables.currentPosition
import com.wingwatch.wingwatcher.GlobalVariables.instructions
import com.wingwatch.wingwatcher.GlobalVariables.observations
import com.wingwatch.wingwatcher.GlobalVariables.tempLat
import com.wingwatch.wingwatcher.GlobalVariables.tempLon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

//----------------------------------------------CODE ATTRIBUTION----------------------------------------------
//Title : Display the user's location activity documentation
//Author: "Mapbox"
//URL: "https://docs.mapbox.com/android/maps/examples/location-tracking/"
class MapActivity : AppCompatActivity() {

    val context: Context = this
    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
        currentPosition = Postion(it.longitude(),it.latitude())
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }
    private lateinit var mapView: MapView
    private lateinit var mapStyle: Style

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val accessToken = getString(R.string.mapbox_access_token)
        val resourceOptions = ResourceOptions.Builder()
            .accessToken(accessToken)
            .build()

        mapView = MapView(this, MapInitOptions(this, resourceOptions))
        setContentView(mapView)

        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }
    }

    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            "mapbox://styles/papilo1/clnvnyemc002101qs2r8011sv"
        ) {
            initLocationComponent()
            setupGesturesListener()
            var count = 0
            for (point in coords) {
                addAnnotationToMap(point.lon,point.lat,point.comName,point.howMany,point.obsDt)
                count++
            }

            for(observation in observations){
                addObservationToMap(observation.lon,observation.lat)
            }
            mapStyle = it
            mapView.location


        }
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@MapActivity,
                    R.drawable.mapbox_user_puck_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@MapActivity,
                    R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    private fun onCameraTrackingDismissed() {
        Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //----------------------------------------------CODE ATTRIBUTION----------------------------------------------
//Title : Annotation documentation
//Author: "Mapbox"
//URL: "https://docs.mapbox.com/android/maps/guides/annotations/annotations/"
    private fun addAnnotationToMap(lon : Double?, lat : Double?, comName : String?, howMany : Int?, obsDt : String?) {

        bitmapFromDrawableRes(
            this@MapActivity,
            R.drawable.red_marker
        )?.let {
            val annotationApi = mapView?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
            var clickCount = 0
            pointAnnotationManager?.addClickListener(object : OnPointAnnotationClickListener {
                override fun onAnnotationClick(annotation: PointAnnotation): Boolean {
                    Log.i("clicked", "$lon,$lat")
                    if(lon != tempLon && lat != tempLat)
                    {
                        tempLon = lon
                        tempLat = lat
                        clickCount =1
                    }
                    else{
                        clickCount++
                    }


                    when (clickCount) {
                        1 -> {
                            instructions.clear()


                            val origin = Point.fromLngLat(currentPosition.lon, currentPosition.lat)
                            val destination = Point.fromLngLat(lon!!, lat!!)
                            val coordinates = "${origin.longitude()},${origin.latitude()}%3B${destination.longitude()},${destination.latitude()}"

                            val mapBoxService = DirectionsClient.retrofit.create(DirectionsApi::class.java)

                            val apiCall = mapBoxService.getData(
                                coordinates = coordinates,
                                alternatives = true,
                                geometries = "geojson",
                                overview = "full",
                                steps = true,
                                language = "en",
                                access_token = "sk.eyJ1IjoicGFwaWxvMSIsImEiOiJjbG53NW9qaWEwNzF3MnRvNjM1Z2xsYTJ1In0.hkYoZC6PIRC6JO3Hy1dT3w")

                            apiCall.enqueue(object : Callback<DirectionsResponse> {
                                override fun onResponse (
                                    call: Call<DirectionsResponse>,
                                    response: Response<DirectionsResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val route: Routes? = response.body()?.routes?.firstOrNull()
                                        if (route != null) {
                                            route.legs.forEach{
                                                it.steps.forEach{
                                                    Log.i("instruction", it.maneuver!!.instruction!!)
                                                    instructions.add(it.maneuver!!.instruction!!)
                                                }
                                            }
                                            val coordinates = route.geometry!!.coordinates.map { Point.fromLngLat(it[0], it[1]) }
                                            val geometry = LineString.fromLngLats(coordinates)
                                            val routeFeature = Feature.fromGeometry(geometry)
                                            val featureCollection =
                                                FeatureCollection.fromFeatures(listOf(routeFeature))

                                            val source = mapStyle.getSourceAs<GeoJsonSource>("route-source")
                                            if (source != null) {
                                                source.featureCollection(featureCollection)
                                            } else {
                                                val sourceBuilder = GeoJsonSource.Builder("route-source")
                                                    .featureCollection(featureCollection)
                                                mapStyle.addSource(sourceBuilder.build())

                                                val lineLayer = lineLayer(
                                                    "route-layer", "route-source") {
                                                    lineCap(LineCap.ROUND)
                                                    lineJoin(LineJoin.ROUND)
                                                    lineOpacity(0.7)
                                                    lineColor(Color.parseColor("#FFAA00"))
                                                    lineWidth(5.0)
                                                }
                                                mapStyle.addLayer(lineLayer)
                                            }

                                        } else {
                                            Log.e("route data", "No route available")
                                        }


                                    }
                                    else{
                                        Log.e("bad req", response.message())
                                    }

                                }

                                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                                    Log.e("API error", "Network error: ${t.message}", t)
                                }
                            })

                        }
                        2 -> {
                                clickCount = 0
                                val intent = Intent(context, ViewAnnotationActivity::class.java)
                                intent.putExtra("lat", lat)
                                intent.putExtra("lon", lon)
                                intent.putExtra("comName", comName)
                                intent.putExtra("howMany", howMany)
                                intent.putExtra("obsDt", obsDt)

                                startActivity(intent)


                        }
                        else -> {
                            clickCount =0
                        }

                    }

                    return true

                }
            })
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(lon!!, lat!!))
                .withIconImage(it)

            pointAnnotationManager?.create(pointAnnotationOptions)

            Log.i("fId", pointAnnotationManager?.create(pointAnnotationOptions)!!.featureIdentifier)
        }
    }

    private fun addObservationToMap(lon : Double?, lat : Double?) {

        bitmapFromDrawableRes(
            this@MapActivity,
            R.drawable.blue_marker
        )?.let {
            val annotationApi = mapView?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
            var clickCount = 0
            pointAnnotationManager?.addClickListener(object : OnPointAnnotationClickListener {
                override fun onAnnotationClick(annotation: PointAnnotation): Boolean {
                    Log.i("clicked", "$lon,$lat")

                    clickCount++

                    when (clickCount) {
                        1 -> {
                            instructions.clear()


                            val origin = Point.fromLngLat(currentPosition.lon, currentPosition.lat)
                            val destination = Point.fromLngLat(lon!!, lat!!)
                            val coordinates = "${origin.longitude()},${origin.latitude()}%3B${destination.longitude()},${destination.latitude()}"

                            val mapBoxService = DirectionsClient.retrofit.create(DirectionsApi::class.java)

                            val apiCall = mapBoxService.getData(
                                coordinates = coordinates,
                                alternatives = true,
                                geometries = "geojson",
                                overview = "full",
                                steps = true,
                                language = "en",
                                access_token = "sk.eyJ1IjoicGFwaWxvMSIsImEiOiJjbG53NW9qaWEwNzF3MnRvNjM1Z2xsYTJ1In0.hkYoZC6PIRC6JO3Hy1dT3w")

                            apiCall.enqueue(object : Callback<DirectionsResponse> {
                                override fun onResponse (
                                    call: Call<DirectionsResponse>,
                                    response: Response<DirectionsResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val route: Routes? = response.body()?.routes?.firstOrNull()
                                        if (route != null) {
                                            route.legs.forEach{
                                                it.steps.forEach{
                                                    Log.i("instruction", it.maneuver!!.instruction!!)
                                                    instructions.add(it.maneuver!!.instruction!!)
                                                }
                                            }
                                            val coordinates = route.geometry!!.coordinates.map { Point.fromLngLat(it[0], it[1]) }
                                            val geometry = LineString.fromLngLats(coordinates)
                                            val routeFeature = Feature.fromGeometry(geometry)
                                            val featureCollection =
                                                FeatureCollection.fromFeatures(listOf(routeFeature))

                                            val source = mapStyle.getSourceAs<GeoJsonSource>("route-source")
                                            if (source != null) {
                                                source.featureCollection(featureCollection)
                                            } else {
                                                val sourceBuilder = GeoJsonSource.Builder("route-source")
                                                    .featureCollection(featureCollection)
                                                mapStyle.addSource(sourceBuilder.build())

                                                val lineLayer = lineLayer(
                                                    "route-layer", "route-source") {
                                                    lineCap(LineCap.ROUND)
                                                    lineJoin(LineJoin.ROUND)
                                                    lineOpacity(0.7)
                                                    lineColor(Color.parseColor("#FFAA00"))
                                                    lineWidth(5.0)
                                                }
                                                mapStyle.addLayer(lineLayer)
                                            }

                                        } else {
                                            Log.e("route data", "No route available")
                                        }


                                    }
                                    else{
                                        Log.e("bad req", response.message())
                                    }

                                }

                                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                                    Log.e("API error", "Network error: ${t.message}", t)
                                }
                            })

                        }
                        2 -> {
                            clickCount =0
                           observations.forEach()
                           {
                               if(it.lat == lat && it.lon == lon)
                               {
                                   val intent = Intent(this@MapActivity, ViewObsActivity::class.java)
                                   intent.putExtra("obs", it)
                                   intent.putExtra("showDirection", true)
                                   startActivity(intent)
                               }
                           }
                        }
                        else -> {
                            clickCount =0
                        }
                    }

                    return true

                }
            })
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()

                .withPoint(Point.fromLngLat(lon!!, lat!!))

                .withIconImage(it)

            pointAnnotationManager?.create(pointAnnotationOptions)


        }
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))
    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {

            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }



}












