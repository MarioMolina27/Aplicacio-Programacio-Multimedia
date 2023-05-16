package com.example.aplicaciomultimedia_mariomolina.activites.video

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aplicaciomultimedia_mariomolina.Keys
import com.example.aplicaciomultimedia_mariomolina.R
import com.example.aplicaciomultimedia_mariomolina.activites.txt.TextActivity
import com.example.aplicaciomultimedia_mariomolina.adapters.FilesAdapter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class VideoActivity : AppCompatActivity() {

    private lateinit var files: ArrayList<File>
    private lateinit var adapter: FilesAdapter

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK)
            {
                val videoUri = result.data?.data
                val moviesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val videoFileName = "MOVIE_${timeStamp}.mp4"
                val videoFile = File(moviesDirectory, videoFileName)
                try
                {
                    val input = contentResolver.openInputStream(videoUri!!)
                    val output = FileOutputStream(videoFile)
                    val buffer = ByteArray(1024)
                    var bytesRead = input?.read(buffer) ?: -1
                    while (bytesRead != -1)
                    {
                        output.write(buffer, 0, bytesRead)
                        bytesRead = input?.read(buffer) ?: -1
                    }
                    input?.close()
                    output.close()
                    files.add(videoFile)
                    adapter.notifyDataSetChanged()
                }
                catch (e: Exception)
                {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val btnCapturaVideo = findViewById<Button>(R.id.btnNouVideo)
        btnCapturaVideo.setOnClickListener()
        {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startForResult.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE))
        }
        val moviesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        val movies = moviesDirectory.listFiles { _, name -> name.endsWith(".mp4") }

        files = ArrayList(movies.asList())
        adapter = FilesAdapter(this,R.layout.txt_item_select,files)

        val lstVideos = findViewById<ListView>(R.id.lstVideos)
        lstVideos.adapter = adapter

        lstVideos.onItemClickListener = AdapterView.OnItemClickListener(){
                _,_,i,_ ->

            val intent = Intent(this, VideoDetail::class.java)
            intent.putExtra(Keys.constKeys.DETAIL_VIDEO,files[i].absolutePath)
            startActivity(intent)
        }
    }

    
}