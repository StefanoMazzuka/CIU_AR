package com.ar.ciu.ciuar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
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
import kotlinx.android.synthetic.main.content_main.*

class Map : AppCompatActivity() {

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap : GoogleMap
    var locations = mutableListOf<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Log.i("STEFANO:", "STEFANO 0")
        var ref = FirebaseDatabase.getInstance().getReference("locations")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                Log.i("STEFANO:", "STEFANO 1")
                for (l in ds.children) {
                    var location = Location()
                    location.name = ds.getValue(String::class.java).toString()
                    location.lat = ds.child("lat").getValue(Double::class.java).toString().toDouble()
                    location.lon = ds.child("lat").getValue(Double::class.java).toString().toDouble()
                    locations.add(location)
                    Log.i("STEFANO:", "STEFANO" + location.name + " " + location.lat)
                }

                mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(OnMapReadyCallback {
                    googleMap = it
                    for (l in locations) {
                        val latLng = LatLng(l.lat, l.lon)
                        Log.i("STEFANO:", "STEFANO" + l.name + " " + l.lat)
                        googleMap.addMarker(MarkerOptions().position(latLng).title(l.name))
                    }
                    val fdi = LatLng(40.453009, -3.733461)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fdi, 15f))
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("KotlinActivity", "Failed to read value.", error.toException())
            }
        })

        /*
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            var ref = FirebaseDatabase.getInstance().getReference("locations")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(ds: DataSnapshot) {

                    for (l in ds.children) {
                        var location = Location()
                        location.name = ds.getValue(String::class.java).toString()
                        location.lat = ds.child("lat").getValue(Double::class.java).toString().toDouble()
                        location.lon = ds.child("lat").getValue(Double::class.java).toString().toDouble()
                        val latLng = LatLng(location.lat, location.lon)
                        googleMap.addMarker(MarkerOptions().position(latLng).title(location.name))
                    }

                    val fdi = LatLng(40.453009, -3.733461)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fdi, 15f))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("KotlinActivity", "Failed to read value.", error.toException())
                }
            })
        })*/
    }
}
