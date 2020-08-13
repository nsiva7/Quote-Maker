package siva.app.quotemaker.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.dashboard.*
import siva.app.quotemaker.R
import siva.app.quotemaker.controls.util.Util

/*
* Created by Siva Nimmala on 11/8/20.
* */
class Dashboard : AppCompatActivity() {


    private val galleryRequest = 123
    private val storagePermissionRequest: Int = 321

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Util.setFullScreen(this)
        setContentView(R.layout.dashboard)

        ivSelectImage.setOnClickListener {
            if (hasPermissions()) {
                startActivityForResult(
                    Intent.createChooser(Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "Select Image"),
                    galleryRequest
                )
            } else {
                requestStoragePermissions()
            }
        }

        ivSavedQuotes.setOnClickListener { startActivity(Intent(this, SavedQuotes::class.java));finish() }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            galleryRequest -> {
                startActivity(Intent(applicationContext, Editor::class.java).putExtra("image", data!!.data))
            }
        }
    }

    fun requestStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), storagePermissionRequest)
        }
    }

    fun hasPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == storagePermissionRequest) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(
                    Intent.createChooser(Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "Select Image"),
                    galleryRequest
                )
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.storage_permission_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}