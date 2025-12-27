package com.guy.class26a_and_3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private var scoresToDisplay: List<Score> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return view
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        // Once the map is ready, check if there are scores to display
        if (scoresToDisplay.isNotEmpty()) {
            showAllScoresOnMap(scoresToDisplay)
        }
    }

    fun focusOnLocation(lat: Double, lon: Double) {
        googleMap?.let {
            val location = LatLng(lat, lon)
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    fun showAllScoresOnMap(scores: List<Score>) {
        this.scoresToDisplay = scores
        googleMap?.let { map ->
            map.clear()
            if (scores.isEmpty()) return@let

            val boundsBuilder = LatLngBounds.Builder()
            scores.forEach { score ->
                val location = LatLng(score.lat, score.lon)
                map.addMarker(MarkerOptions().position(location).title("Distance: ${score.distance}"))
                boundsBuilder.include(location)
            }
            val bounds = boundsBuilder.build()
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }
}