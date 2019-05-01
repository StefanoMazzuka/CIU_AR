package com.ar.ciu.ciuar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        increase.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://ucm.es/ciumadrid/crecimiento-de-la-ciudad-universitaria"))
            startActivity(i)
        }

        war.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ucm.es/ciumadrid/guerra-en-la-ciudad-universitaria"))
            startActivity(i)
        }

        introduction.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ucm.es/ciumadrid/historia"))
            startActivity(i)
        }

        origin.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ucm.es/ciumadrid/origenes"))
            startActivity(i)
        }

        firstPart.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ucm.es/ciumadrid/primera-etapa"))
            startActivity(i)
        }

        secondPart.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ucm.es/ciumadrid/segunda-etapa"))
            startActivity(i)
        }

        thirdPart.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ucm.es/ciumadrid/reconstruccionciumadrid"))
            startActivity(i)
        }
    }
}
