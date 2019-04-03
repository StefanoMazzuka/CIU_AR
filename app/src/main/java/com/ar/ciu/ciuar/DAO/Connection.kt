package com.ar.ciu.ciuar.DAO

import com.google.firebase.database.*

/**
 * This class implements the DAO manager.
 */
class FirebaseDAOManager {

    lateinit var dataBase: FirebaseDatabase

    //Devuelve una instancia de la base de datos, manteniendo un solo hilo
    fun getInstance(dataBaseName: String): FirebaseDatabase? {

        synchronized(FirebaseDAOManager::class.java) {
            dataBase = FirebaseDatabase.getInstance()
        }

        return dataBase
    }
}


