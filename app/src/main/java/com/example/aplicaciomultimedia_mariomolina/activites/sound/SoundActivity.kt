package com.example.aplicaciomultimedia_mariomolina.activites.sound

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
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
import androidx.annotation.RequiresApi
import com.example.aplicaciomultimedia_mariomolina.Keys
import com.example.aplicaciomultimedia_mariomolina.R
import com.example.aplicaciomultimedia_mariomolina.activites.img.ImatgeEdit
import com.example.aplicaciomultimedia_mariomolina.activites.video.VideoDetail
import com.example.aplicaciomultimedia_mariomolina.adapters.FilesAdapter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SoundActivity : AppCompatActivity() {

    private lateinit var files: ArrayList<File>
    private lateinit var adapter: FilesAdapter

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK)
            {
                val videoUri = result.data?.data
                val musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val audioFileName = "AUDIO_${timeStamp}.mp3"
                val audioFile = File(musicDirectory, audioFileName)
                try
                {
                    val input = contentResolver.openInputStream(videoUri!!)
                    val output = FileOutputStream(audioFile)
                    val buffer = ByteArray(1024)
                    var bytesRead = input?.read(buffer) ?: -1
                    while (bytesRead != -1)
                    {
                        output.write(buffer, 0, bytesRead)
                        bytesRead = input?.read(buffer) ?: -1
                    }
                    input?.close()
                    output.close()
                    files.add(audioFile)
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
        setContentView(R.layout.activity_sound)
        val musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val buttonStart = findViewById<Button>(R.id.buttonStart)



        buttonStart.setOnClickListener()
        {
            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
            startForResult.launch(intent)
        }

        val movies = musicDirectory.listFiles { _, name -> name.endsWith(".mp3") }

        files = ArrayList(movies.asList())
        adapter = FilesAdapter(this,R.layout.txt_item_select,files)

        val lstAudio = findViewById<ListView>(R.id.lstAudio)
        lstAudio.adapter = adapter

        lstAudio.onItemClickListener = AdapterView.OnItemClickListener()
        {
                _,_,i,_ ->
            val file = files[i]
            val mediaPlayer = MediaPlayer.create(this, Uri.parse(file.path))
            mediaPlayer.start()
        }
    }
}