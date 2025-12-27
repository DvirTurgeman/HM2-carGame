package com.guy.class26a_and_3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.guy.class26a_and_3.databinding.FragmentMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: MapView
    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(requireContext()))

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        map = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        enableMyLocation()

        // Get scores from arguments and display them
        val scoresJson = arguments?.getString("scores_json")
        if (scoresJson != null) {
            val scores: List<Score> = Gson().fromJson(scoresJson, object : TypeToken<List<Score>>() {}.type)
            showAllScoresOnMap(scores)
        }

        return binding.root
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
            locationOverlay.enableMyLocation()
            map.overlays.add(locationOverlay)
            zoomToCurrentLocation(true) // Zoom to current location on startup
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun zoomToCurrentLocation(animate: Boolean) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentGeoPoint = GeoPoint(location.latitude, location.longitude)
                    if(animate) {
                        map.controller.animateTo(currentGeoPoint)
                    } else {
                        map.controller.setCenter(currentGeoPoint)
                    }
                    map.controller.setZoom(18.0)
                }
            }
        }
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    fun focusOnLocation(lat: Double, lon: Double) {
        val geoPoint = GeoPoint(lat, lon)
        map.controller.animateTo(geoPoint)
        map.controller.setZoom(18.0)
    }

    private fun showAllScoresOnMap(scores: List<Score>) {
        if (scores.isEmpty()) {
             zoomToCurrentLocation(false) // If no scores, just zoom to current location
            return
        }

        map.overlays.removeAll { it !is MyLocationNewOverlay } // Clear only score markers

        val geoPoints = scores.map { score ->
            val geoPoint = GeoPoint(score.lat, score.lon)
            val marker = Marker(map)
            marker.position = geoPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "Distance: ${score.distance}, Coins: ${score.coins}"
            // marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_marker)
            map.overlays.add(marker)
            geoPoint
        }

        map.post {
            val boundingBox = BoundingBox.fromGeoPoints(geoPoints)
            map.zoomToBoundingBox(boundingBox, true, 100)
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}