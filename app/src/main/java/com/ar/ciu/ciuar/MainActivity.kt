package com.ar.ciu.ciuar

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ar.ciu.ciuar.classifier.*
import com.ar.ciu.ciuar.classifier.tensorflow.ImageClassifierFactory
import com.ar.ciu.ciuar.utils.getCroppedBitmap
import com.ar.ciu.ciuar.utils.getUriFromFilePath
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import android.widget.Toast
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*
import kotlin.collections.ArrayList

private const val REQUEST_PERMISSIONS = 1
private const val REQUEST_TAKE_PICTURE = 2
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val handler = Handler()
    private lateinit var classifier: Classifier
    private var photoFilePath = ""
    private lateinit var tts: TextToSpeech
    private var toSpeak = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        imageButton.setOnClickListener { takePhoto() }

        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            var loc = Locale("es")
            if (status != TextToSpeech.ERROR) tts.language = loc
        })

        fab.setImageResource(R.drawable.ic_volume_on)
        fab.setOnClickListener {
            if (toSpeak == "") Toast.makeText(this, "No tengo nada que decir...", Toast.LENGTH_SHORT).show()
            else if (tts.isSpeaking) tts.stop()
            else tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    private fun checkPermissions() {
        if (arePermissionsAlreadyGranted()) init()
        else requestPermissions()
    }

    private fun arePermissionsAlreadyGranted() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS && arePermissionGranted(grantResults)) {
            init()
        } else {
            requestPermissions()
        }
    }

    private fun arePermissionGranted(grantResults: IntArray) =
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

    private fun init() {
        createClassifier()
    }

    private fun createClassifier() {
        classifier = ImageClassifierFactory.create(
                assets,
                GRAPH_FILE_PATH,
                LABELS_FILE_PATH,
                IMAGE_SIZE,
                GRAPH_INPUT_NAME,
                GRAPH_OUTPUT_NAME
        )
    }

    private fun takePhoto() {
        photoFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/${System.currentTimeMillis()}.jpg"
        val currentPhotoUri = getUriFromFilePath(this, photoFilePath)

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
        takePictureIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val file = File(photoFilePath)
        if (requestCode == REQUEST_TAKE_PICTURE && file.exists()) {
            classifyPhoto(file)
        }
    }

    private fun classifyPhoto(file: File) {
        val photoBitmap = BitmapFactory.decodeFile(file.absolutePath)
        val croppedBitmap = getCroppedBitmap(photoBitmap)
        classifyAndShowResult(croppedBitmap)
        imagePhoto.setImageBitmap(photoBitmap) // Añadirmos la foto al layout
    }

    private fun classifyAndShowResult(croppedBitmap: Bitmap) {
        drawer_layout.setBackgroundResource(0)
        runInBackground(
                Runnable {
                    val result = classifier.recognizeImage(croppedBitmap)
                    if (result.confidence >= 0.7) showResult(result)
                    else showNotFound(result)
                })
    }

    @Synchronized
    private fun runInBackground(runnable: Runnable) {
        handler.post(runnable)
    }

    private fun showNotFound(result: Result) {
        var monumentID = when {
            result.result == "alexander" -> "la estatua de Alexander Dubcek"
            result.result == "antorcha" -> "la estatua de Los Portadores de la Antorcha"
            result.result == "camilo" -> "la estatua de Camilo José Cela"
            result.result == "fdi" -> "la Facultad de Informática"
            result.result == "geografia" -> "la Facultad de Geografía e Historia"
            result.result == "multiusos" -> "el edificio Multiusos"
            result.result == "omar" -> "la estatua de Omar Jaymay"
            else -> "rectorado"
        }

        toSpeak = "Lo siento, no estoy segura de que es esto pero creo que es " + monumentID + " en un " + ((result.confidence * 100).toInt()) + "%"
        textResult.text = "Ups..."
        authorLabel.text = ""
        buildingDateLabel.text = ""
        lastMonidificationLabel.text = ""
        author.text = ""
        buildingDate.text = ""
        lastMonidification.text = ""

        description.text =  toSpeak
    }

    private fun showResult(result: Result) {

        var monumentID = result.result
        var conn = Connection()
        conn.getMonument(monumentID, object : FirebaseCallback {
            override fun monument(monument: Monument) {
                //Do what you need to do with your list
                textResult.text = monument.title
                toSpeak = monument.description

                authorLabel.text = "AUTOR:"
                buildingDateLabel.text = "FECHA DE CREACIÓIN:"
                lastMonidificationLabel.text = "FECHA REMODELACIÓN:"
                author.text = monument.author
                description.text = monument.description
                buildingDate.text = monument.buildingDate
                lastMonidification.text = monument.lastModification
            }

            override fun monuments(list: ArrayList<Monument>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun locations(list: ArrayList<Location>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    // Menu
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.takaPhoto -> {
                takePhoto()
            }
            R.id.map -> {
                val intent = Intent(this, Map::class.java)
                startActivity(intent)
            }
            R.id.monuments -> {
                val intent = Intent(this, MonumentsActivity::class.java)
                startActivity(intent)
            }
            R.id.history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.contact -> {
                displayContact()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displayContact() {
        // Initialize a new instance of
        val contactView = AlertDialog.Builder(this@MainActivity)

        // Set the alert dialog title
        contactView.setTitle("Contacto")

        // Display a message on alert dialog
        contactView.setMessage("Autores: " + '\n' + "Stefano Mazzuka" + '\n' + "Arturo Marino Quintana" + '\n' + '\n'
                + "Email:" + '\n' + "stefano.mazzuka@gmail.com" + '\n' + "arturomarino92@gmail.com")

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = contactView.create()

        // Display the alert dialog on app interface
        dialog.show()
    }
}