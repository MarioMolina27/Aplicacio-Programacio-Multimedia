package com.example.aplicaciomultimedia_mariomolina.activites.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.aplicaciomultimedia_mariomolina.R
import com.example.aplicaciomultimedia_mariomolina.activites.img.ImatgeActivity
import com.example.aplicaciomultimedia_mariomolina.activites.intents.IntentsActivity
import com.example.aplicaciomultimedia_mariomolina.activites.sound.SoundActivity
import com.example.aplicaciomultimedia_mariomolina.activites.txt.SeleccionarText
import com.example.aplicaciomultimedia_mariomolina.activites.video.VideoActivity

class MainActivity : AppCompatActivity() {

    object PERMISSIONS
    {
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO)
    }
    private var permissionsGranted = false;

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            if(Build.VERSION.SDK_INT>=30)
            {
                if(Environment.isExternalStorageManager())
                {
                    start()
                }
                else
                {
                    Toast.makeText(this,"S'han de donar permisos a la aplicació per accedir als arxius", Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
        checkAllFilesPermissions()

        val btnVisTxt = findViewById<Button>(R.id.btnVisTxt)
        val btnNewImatge = findViewById<Button>(R.id.btnNewImatge)
        val btnVideo = findViewById<Button>(R.id.btnVideo)
        val btnSound =  findViewById<Button>(R.id.btnSound)
        val btnIntents = findViewById<Button>(R.id.btnIntents)

        btnVisTxt.setOnClickListener()
        {
            if(permissionsGranted)
            {
                val intent = Intent(this, SeleccionarText::class.java)
                startActivity(intent)
            }
        }

        btnVideo.setOnClickListener()
        {
            if(permissionsGranted)
            {
                val intent = Intent(this, VideoActivity::class.java)
                startActivity(intent)
            }
        }

        btnNewImatge.setOnClickListener()
        {
            if(permissionsGranted)
            {
                val intent = Intent(this,ImatgeActivity::class.java)
                startActivity(intent)
            }
        }

        btnSound.setOnClickListener()
        {
            if(permissionsGranted)
            {
                val intent = Intent(this,SoundActivity::class.java)
                startActivity(intent)
            }
        }

        btnIntents.setOnClickListener()
        {
            if(permissionsGranted)
            {
                val intent = Intent(this,IntentsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkPermissions(){
        if(Build.VERSION.SDK_INT>23)
        {
            if(checkPermissions(PERMISSIONS.PERMISSIONS))
            {
                start()
            }
            else
            {
                requestPermissions(PERMISSIONS.PERMISSIONS)
            }
        }
        else
        {
            start()
        }
    }

    private fun checkPermissions(permissions: Array<String>): Boolean
    {
        var checked: Boolean = true
        var i: Int =0

        while(i<permissions.size && checked)
        {
            if(ContextCompat.checkSelfPermission(this,permissions[i])!= PackageManager.PERMISSION_GRANTED)
            {
                checked = false
            }
            i++
        }
        return checked
    }

    private fun checkAllFilesPermissions()
    {
        if(Build.VERSION.SDK_INT>=30)
        {
            if(Environment.isExternalStorageManager())
            {
                start()
            }
            else
            {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                getResult.launch(intent)

            }
        }
        else
        {
            start()
        }
    }

    private fun requestPermissions(permissions: Array<String>)
    {
        ActivityCompat.requestPermissions(this,permissions,1)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var checked: Boolean = true
        var i: Int =0

        while(i<permissions.size && checked)
        {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
            {
                checked = false
            }
            i++
        }

        if(checked)
        {
            start()
        }
        else
        {
            Toast.makeText(this,"PERMISSIONS NOT WERE GRANTED", Toast.LENGTH_LONG).show()
        }
    }

    private fun start()
    {
        permissionsGranted = true
    }
}