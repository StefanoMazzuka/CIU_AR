package com.ar.ciu.ciuar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log

class Connection {

    fun getMonument(monumentID : String, firebaseCallback: FirebaseCallback) {
        var ref = FirebaseDatabase.getInstance().getReference("monuments")
        ref.child(monumentID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                var monument = Monument()
                monument.author = ds.child("autor").getValue(String::class.java).toString()
                monument.description = ds.child("descripcion").getValue(String::class.java).toString()
                monument.buildingDate = ds.child("fechaConstruccion").getValue(String::class.java).toString()
                monument.img = ds.child("imagen").getValue(String::class.java).toString()
                monument.title = ds.child("titulo").getValue(String::class.java).toString()
                monument.lastModification = ds.child("ultimaModificacion").getValue(String::class.java).toString()

                firebaseCallback.monument(monument)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("KotlinActivity", "Failed to read value.", error.toException())
            }
        })
    }

    fun getMonuments(firebaseCallback: FirebaseCallback) {
        var ref = FirebaseDatabase.getInstance().getReference("monuments")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {

                val monuments = arrayListOf<Monument>()
                for (m in ds.children) {
                    var monument = Monument()
                    monument.author = m.child("autor").getValue(String::class.java).toString()
                    monument.description = m.child("descripcion").getValue(String::class.java).toString()
                    monument.buildingDate = m.child("fechaConstruccion").getValue(String::class.java).toString()
                    monument.img = m.child("imagen").getValue(String::class.java).toString()
                    monument.title = m.child("titulo").getValue(String::class.java).toString()
                    monument.lastModification = m.child("ultimaModificacion").getValue(String::class.java).toString()
                    monuments.add(monument)
                }
                firebaseCallback.monuments(monuments)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("KotlinActivity", "Failed to read value.", error.toException())
            }
        })
    }

    fun getLocations(firebaseCallback: FirebaseCallback) {
        var ref = FirebaseDatabase.getInstance().getReference("locations")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                val locations = arrayListOf<Location>()
                locations.clear()
                for (l in ds.children) {
                    var location = Location()
                    location.name = l.child("ubicación").getValue(String::class.java).toString()
                    location.lat = l.child("lat").getValue(Double::class.java).toString().toDouble()
                    location.lon = l.child("lon").getValue(Double::class.java).toString().toDouble()
                    locations.add(location)
                }
                firebaseCallback.locations(locations)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("KotlinActivity", "Failed to read value.", error.toException())
            }
        })
    }
}