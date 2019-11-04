package com.example.aviasalesanim.ui

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.os.Bundle
import android.util.Property
import androidx.lifecycle.Observer
import com.example.aviasalesanim.domain.model.Location
import com.example.aviasalesanim.ui.search.SearchCityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private val scope = MainScope()

    private val viewModel: SearchCityViewModel by sharedViewModel()

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.departureAndDestination.observe(viewLifecycleOwner, Observer {
            setupMap(it, googleMap)
        })
    }

    private fun setupMap(departureAndDestination: Pair<Location, Location>, googleMap: GoogleMap) {
        val (departure, destination) = departureAndDestination

        val start = LatLng(departure.lat, departure.lon)
        val end = LatLng(destination.lat, destination.lon)
        val p1 = LatLng(end.latitude, start.longitude)
        val p2 = LatLng(start.latitude, end.longitude)

        val points = arrayOf(start, p1, p2, end)

        zoomInOrOut(points, googleMap)
        drawPolyline(points, googleMap)

        val marker = googleMap.addMarker(MarkerOptions().position(start))
        moveMarker(points, marker)
    }

    private fun zoomInOrOut(points: Array<LatLng>, googleMap: GoogleMap) {
        val boundsBuilder = LatLngBounds.Builder()
        points.forEach { boundsBuilder.include(it) }

        val padding = 100
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), padding))
    }

    private fun drawPolyline(points: Array<LatLng>, googleMap: GoogleMap) {
        val polylineOptions = PolylineOptions().apply {
            color(0xffe84c65.toInt())
            pattern(listOf(Dot(), Gap(10f)))
        }
        for (i in 0..10) {
            val t = i / 10.0
            drawCurve(points, t) { latLng ->
                polylineOptions.add(latLng)
            }
        }
        googleMap.addPolyline(polylineOptions)
    }

    private fun moveMarker(points: Array<LatLng>, marker: Marker) {
        val values = Array(11) { LatLng(0.0, 0.0) }
        for (i in 0..10) {
            val t = i / 10.0
            drawCurve(points, t) { latLng ->
                values[i] = latLng
            }
        }
        animateMarker(marker, values)
    }

    private fun drawCurve(
        points: Array<LatLng>,
        fraction: Double,
        draw: ((point: LatLng) -> Unit)
    ) {
        if (points.size == 1) {
            draw(points[0])

            return
        }

        val newPoints = Array(points.size - 1) { i ->
            val lat = (1 - fraction) * points[i].latitude + fraction * points[i + 1].latitude
            val lon = (1 - fraction) * points[i].longitude + fraction * points[i + 1].longitude
            LatLng(lat, lon)
        }
        drawCurve(newPoints, fraction, draw)
    }

    private fun animateMarker(marker: Marker, points: Array<LatLng>) {
        val typeEvaluator = TypeEvaluator<LatLng> { fraction: Float, from: LatLng, to: LatLng ->
            SphericalUtil.interpolate(from, to, fraction.toDouble())
        }
        val property = Property.of(Marker::class.java, LatLng::class.java, "position")
        val animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, *points)
        animator.duration = 10000
        animator.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}