package com.ar.ciu.ciuar.DAO

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ar.ciu.ciuar.History
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/**
 * This class
 */
class DAOHistory (dataBase: FirebaseDatabase) : AppCompatActivity() {

    var historyPages = arrayListOf<History>()

    fun createHistory(){

    }

    /**
     * Load all the history values
     */
    fun loadAllHistory(){

        var ref = dataBase.getReference("history")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {

                historyPages.clear()

                for (l in ds.children) {
                    var history = History()

                    history.title = l.child("titulo").getValue(String::class.java).toString()
                    history.description = l.child("Descripción").getValue(String::class.java).toString()
                    history.url = l.child("URL").getValue(String::class.java).toString()
                    history.lastModification = l.child("ultimaModificacion").getValue(String::class.java).toString()
                    history.image = ""

                    historyPages.add(history)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("KotlinActivity", "Failed to read locations.", error.toException())
            }
        })
    }

    fun loadHistory(){

    }

    fun deleteHistory(){

    }

    fun reset(){

    }
}