package com.ar.ciu.ciuar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Map : AppCompatActivity() {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    var locations = arrayListOf<Location>()
    lateinit var dataBase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        loadLocations()

        // Toast.makeText(this, l.lon.toString(), Toast.LENGTH_LONG).show()
    }

    fun loadLocations() {
        dataBase = FirebaseDatabase.getInstance()
        var ref = dataBase.getReference("locations")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {

                locations.clear()
                for (l in ds.children) {
                    var location = Location()
                    location.name = l.child("ubicación").getValue(String::class.java).toString()
                    location.lat = l.child("lat").getValue(Double::class.java).toString().toDouble()
                    location.lon = l.child("lon").getValue(Double::class.java).toString().toDouble()
                    locations.add(location)
                }

                // AQUI EL MAPS
                displayMap()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("KotlinActivity", "Failed to read value.", error.toException())
            }
        })
    }

    fun displayMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            googleMap.clear()
            for (l in locations) {
                val latLng = LatLng(l.lat, l.lon)
                googleMap.addMarker(MarkerOptions().position(latLng).title(l.name))

            }
            val fdi = LatLng(40.453009, -3.733461)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fdi, 15f))
        })
    }
}
