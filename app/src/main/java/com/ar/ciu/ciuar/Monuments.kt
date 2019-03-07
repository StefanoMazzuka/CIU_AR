package com.ar.ciu.ciuar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_monuments.*

class Monuments : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monuments)
/*
        // Reference to an image file in Cloud Storage
        val storageReference = FirebaseStorage.getInstance().reference

        GlideApp.with(this /* context */)
                .load(storageReference)
                .into(image_view)
*/
    }
}
