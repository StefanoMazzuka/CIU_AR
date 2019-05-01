package com.ar.ciu.ciuar

interface FirebaseCallback {
    fun monument(monument: Monument)
    fun monuments(list: ArrayList<Monument>)
    fun locations(list: ArrayList<Location>)
}