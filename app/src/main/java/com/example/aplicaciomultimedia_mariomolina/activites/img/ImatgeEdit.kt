package com.example.aplicaciomultimedia_mariomolina.activites.img

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicaciomultimedia_mariomolina.Keys
import com.example.aplicaciomultimedia_mariomolina.R
import com.gowtham.library.utils.TrimVideo
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ImatgeEdit : AppCompatActivity() {

    private lateinit var imageDetail: ImageView
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imatge_edit)

        imageDetail =  findViewById(R.id.imageDetail)
        val buttonEditImage = findViewById<Button>(R.id.buttonEditImage)
        val btnGuardarImatgeEdit = findViewById<Button>(R.id.btnGuardarImatgeEdit)

        val intent = getIntent()
        val imagePath = intent.getStringExtra(Keys.constKeys.EDIT_IMAGE)
        imageUri = Uri.fromFile(File(imagePath))
        val bitmap = BitmapFactory.decodeFile(imagePath)
        imageDetail.setImageBitmap(bitmap)


        buttonEditImage.setOnClickListener {
            CropImage.activity(imageUri)
                .start(this);
        }

        btnGuardarImatgeEdit.setOnClickListener()
        {
            saveNewImage()
            val intent = Intent(this,ImatgeActivity::class.java)
            startActivity(intent)
        }
    }

    fun saveNewImage()
    {
        val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}.jpg"
        val imageFile = File(picturesDirectory, imageFileName)
        val outputStream = FileOutputStream(imageFile)
        val input = contentResolver.openInputStream(imageUri)
        val buffer = ByteArray(1024)
        var len: Int
        while (input?.read(buffer).also { len = it!! } != -1) {
            outputStream.write(buffer, 0, len)
        }
        input?.close()
        outputStream.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK)
            {
                imageUri = result.uri
                imageDetail.setImageURI(imageUri)
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Toast.makeText(this, "La imatge no s'ha editat", Toast.LENGTH_LONG).show()
            }
        }
    }


}