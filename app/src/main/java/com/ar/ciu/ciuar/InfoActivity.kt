package com.ar.ciu.ciuar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        var result: String = intent.getStringExtra("result")
        var confidence: String = intent.getStringExtra("confidence")
        val textViewResult : TextView = findViewById(R.id.result) as TextView
        val textViewConfidence : TextView = findViewById(R.id.confidence) as TextView
        textViewResult.setText(result)
        textViewConfidence.setText(confidence)
    }
}
