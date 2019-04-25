package com.ar.ciu.ciuar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout

class MonumentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monuments)

        val recyclerView = findViewById(R.id.monumentsRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        var conn = Connection()
        conn.getMonuments(object : FirebaseCallback {
            override fun monuments(monuments: ArrayList<Monument>) {
                //Do what you need to do with your list
                val adapter = MonumentsViewAdapter(monuments, this@MonumentsActivity)
                recyclerView.adapter = adapter
            }

            override fun locations(list: ArrayList<Location>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun monument(monument: Monument) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}