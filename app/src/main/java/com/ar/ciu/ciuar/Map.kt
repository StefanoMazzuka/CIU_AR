package com.ar.ciu.ciuar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

class Map : AppCompatActivity() {

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("locations")
        

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //val value = dataSnapshot.getValue(HashMap::class.java)
                Log.d("KotlinActivity", "---------------------------------------Value is")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("KotlinActivity", "Failed to read value.", error.toException())
            }
        })



        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            // Cambiar nombres variables
            val caballo = LatLng(40.443800, -3.726051)
            val estatuaalexanderdubcek = LatLng(40.449457, -3.730679)
            val estatuacamilojosecela = LatLng(40.449707, -3.730075)
            val estatuaomarjayyam = LatLng(40.449471, -3.730536)
            val fdi = LatLng(40.453009, -3.733461)
            val geografiaehistoria = LatLng(40.449263, -3.734627)
            val multiusos = LatLng(40.449845, -3.732986)
            val rectorado = LatLng(40.437605, -3.725227)

            googleMap.addMarker(MarkerOptions().position(caballo).title("Los portadores de la antorcha"))
            googleMap.addMarker(MarkerOptions().position(fdi).title("Facultad de Informática"))
            googleMap.addMarker(MarkerOptions().position(estatuaalexanderdubcek).title("Alexander Dubcek"))
            googleMap.addMarker(MarkerOptions().position(estatuacamilojosecela).title("Camilo Jose Cela"))
            googleMap.addMarker(MarkerOptions().position(estatuaomarjayyam).title("Omar Jayyam"))
            googleMap.addMarker(MarkerOptions().position(geografiaehistoria).title("Facultad de Geografía e Historia"))
            googleMap.addMarker(MarkerOptions().position(multiusos).title("Edificio Multiusos"))
            googleMap.addMarker(MarkerOptions().position(rectorado).title("Rectorado"))

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fdi, 15f))
        })
    }
}
