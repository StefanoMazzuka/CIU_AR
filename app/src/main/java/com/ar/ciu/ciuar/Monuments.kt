package com.ar.ciu.ciuar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Monuments : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monuments)

        val recyclerView = findViewById(R.id.monumentsRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        var ref = FirebaseDatabase.getInstance().getReference("monuments")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {

                val monuments = arrayListOf<Monument>()
                for (m in ds.children) {
                    var monument = Monument()
                    monument.audio = m.child("audio").getValue(String::class.java).toString()
                    monument.author = m.child("autor").getValue(String::class.java).toString()
                    monument.description = m.child("descripcion").getValue(String::class.java).toString()
                    monument.buildingDate = m.child("fechaConstruccion").getValue(String::class.java).toString()
                    monument.img = m.child("imagen").getValue(String::class.java).toString()
                    monument.title = m.child("titulo").getValue(String::class.java).toString()
                    monument.lastModification = m.child("ultimaModificacion").getValue(String::class.java).toString()
                    monuments.add(monument)
                }

                val adapter = MonumentsViewAdapter(monuments)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("KotlinActivity", "Failed to read value.", error.toException())
            }
        })
    }
}