package com.example.aplicaciomultimedia_mariomolina.activites.img

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.aplicaciomultimedia_mariomolina.Keys
import com.example.aplicaciomultimedia_mariomolina.R
import com.example.aplicaciomultimedia_mariomolina.adapters.GalleryAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImatgeActivity : AppCompatActivity() {

    var images = ArrayList<String>()
    private lateinit var customAdapter: GalleryAdapter

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val image = result.data?.extras?.get("data") as Bitmap?
                val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val imageFileName = "JPEG_${timeStamp}.jpg"
                val imageFile = File(picturesDirectory, imageFileName)
                images.add(imageFile.absolutePath)
                customAdapter.notifyDataSetChanged()
                val contentResolver = applicationContext.contentResolver
                val outputStream = contentResolver.openOutputStream(Uri.fromFile(imageFile))
                image?.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream?.close()
                Toast.makeText(this, "Imatge guardada satisfactoriament", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imatge)

        val newImage = findViewById<Button>(R.id.btnCapturaImg)
        val gridView = findViewById<GridView>(R.id.grdImgPictures)
        images = getAllImages()

        customAdapter = GalleryAdapter(this, R.layout.image_item,images)

        gridView.adapter = customAdapter


        newImage.setOnClickListener()
        {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        gridView.onItemClickListener = AdapterView.OnItemClickListener()
        {
                _,_,i,_ ->
            val intent = Intent(this, ImatgeEdit::class.java)
            intent.putExtra(Keys.constKeys.EDIT_IMAGE,images[i])
            startActivity(intent)
        }
    }

    private fun getAllImages(): ArrayList<String> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media.MIME_TYPE + " = ?"
        val selectionArgs = arrayOf("image/jpeg")
        val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val imagePath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                images.add(imagePath)
            }
            cursor.close()
        }

        return images
    }
}