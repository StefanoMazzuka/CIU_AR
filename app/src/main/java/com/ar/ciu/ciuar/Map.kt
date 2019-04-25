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
    lateinit var dataBase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        var conn = Connection()
        conn.getLocations(object : FirebaseCallback {
            override fun locations(locations: ArrayList<Location>) {
                //Do what you need to do with your list
                displayMap(locations)
            }

            override fun monuments(list: ArrayList<Monument>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun monument(monument: Monument) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        // Toast.makeText(this, l.lon.toString(), Toast.LENGTH_LONG).show()
    }

    fun displayMap(locations: ArrayList<Location>) {
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
