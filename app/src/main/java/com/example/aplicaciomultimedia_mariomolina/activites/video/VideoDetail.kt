package com.example.aplicaciomultimedia_mariomolina.activites.video

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicaciomultimedia_mariomolina.Keys
import com.example.aplicaciomultimedia_mariomolina.R
import com.gowtham.library.utils.CompressOption
import com.gowtham.library.utils.TrimVideo
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class VideoDetail : AppCompatActivity() {

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK && result.getData() != null) {
            val videoPath = result.data?.extras?.getString(TrimVideo.TRIMMED_VIDEO_PATH)

            videoPath?.let {
                val file = File(it)
                try {
                    val inputStream = FileInputStream(file)
                    val nombreArchivo = file.name
                    val directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                    val nuevoArchivo = File(directorio, nombreArchivo)
                    val outputStream = FileOutputStream(nuevoArchivo)
                    inputStream.copyTo(outputStream)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            val intent = Intent(this,VideoActivity::class.java)
            startActivity(intent)
        }
    }
    private lateinit var videoView: VideoView
    private lateinit var uri:Uri
    var mediaControls: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)
        videoView = findViewById<VideoView>(R.id.videoView)
        val buttonEditarVideo = findViewById<Button>(R.id.buttonEditarVideo)

        val intent = getIntent()
        val videoPath = intent.getStringExtra(Keys.constKeys.DETAIL_VIDEO)
        uri = Uri.fromFile(File(videoPath))

        if (mediaControls == null) {
            mediaControls = MediaController(this)
            videoView.setMediaController(mediaControls)
            videoView.setVideoPath(videoPath)
            videoView.start()
        }

        buttonEditarVideo.setOnClickListener()
        {
            TrimVideo.activity(videoPath)
                .setCompressOption(CompressOption(30,"1M",460,320))
                .setHideSeekBar(true)
                .start(this,startForResult);
        }
    }
}