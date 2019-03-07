package com.ar.ciu.ciuar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
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
import android.text.SpannableString
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_main.*

private const val REQUEST_PERMISSIONS = 1
private const val REQUEST_TAKE_PICTURE = 2
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private val handler = Handler()
    private lateinit var classifier: Classifier
    private var photoFilePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

       val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        checkPermissions()
    }

    private fun checkPermissions() {
        if (arePermissionsAlreadyGranted()) {
            init()
        } else {
            requestPermissions()
        }
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
        //takePhoto()
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
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.takaPhoto) {
            takePhoto()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }*/

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
        runInBackground(
                Runnable {
                    val result = classifier.recognizeImage(croppedBitmap)
                    showResult(result)
                })
    }

    @Synchronized
    private fun runInBackground(runnable: Runnable) {
        handler.post(runnable)
    }

    //Prueba subida commit
    private fun showResult(result: Result) {

        var monumentID = when {
            result.result == "estatuaalexanderdubcek" -> "alexander"
            result.result == "caballo" -> "antorcha"
            result.result == "estatuacamilojosecela" -> "camilo"
            result.result == "fdi" -> "fdi"
            result.result == "geografiaehistoria" -> "geografia"
            result.result == "multiusos" -> "multiusos"
            result.result == "estatuaomarjayyam" -> "omar"
            else -> "rectorado"
        }

        var ref = FirebaseDatabase.getInstance().getReference("monuments")
        ref.child(monumentID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                var monument = Monument()
                monument.audio = ds.child("audio").getValue(String::class.java).toString()
                monument.author = ds.child("autor").getValue(String::class.java).toString()
                monument.description = ds.child("descripcion").getValue(String::class.java).toString()
                monument.buildingDate = ds.child("fechaConstrucción").getValue(String::class.java).toString()
                monument.img = ds.child("imagen").getValue(String::class.java).toString()
                monument.title = ds.child("titulo").getValue(String::class.java).toString()
                monument.lastModification = ds.child("ultimaModificacion").getValue(String::class.java).toString()

                textResult.text = monument.title
                val ss = SpannableString(monument.description)
                ss.setSpan(MyLeadingMarginSpan2(10, 600), 0, ss.length, 0)
                textInfo.text = ss
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("KotlinActivity", "Failed to read value.", error.toException())
            }
        })
        //layoutContainer.setBackgroundColor(getColorFromResult(result.result))
    }

/*@Suppress("DEPRECATION")
prate fun getColorFromResult(result: String): Int {
    return if (result == getString(R.string.daisy)) {
        resources.getColor(R.color.daisy)
    } else if (result == getString(R.string.dandelion)) {
        resources.getColor(R.color.dandelion)
    } else if (result == getString(R.string.millenniumfalcon)) {
        resources.getColor(R.color.millenniumfalcon)
    } else if (result == getString(R.string.roses)) {
        resources.getColor(R.color.roses)
    } else if (result == getString(R.string.sunflowers)) {
        resources.getColor(R.color.sunflowers)
    } else {
        resources.getColor(R.color.tulips)
    }
}*/

    //Menu

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
                val intent = Intent(this, Monuments::class.java)
                startActivity(intent)
            }
            R.id.history -> {

            }
            R.id.settings -> {

            }
            R.id.contact -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}