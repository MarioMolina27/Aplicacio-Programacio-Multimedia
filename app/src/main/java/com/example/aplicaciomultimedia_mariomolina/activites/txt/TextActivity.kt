package com.example.aplicaciomultimedia_mariomolina.activites.txt

import android.app.Dialog
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.aplicaciomultimedia_mariomolina.Keys
import com.example.aplicaciomultimedia_mariomolina.MyFile
import com.example.aplicaciomultimedia_mariomolina.R
import java.io.IOException

class TextActivity : AppCompatActivity() {

    private lateinit var txtText:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_captura_text)

        val filePath = intent.getStringExtra(Keys.constKeys.SELECT_FILE)
        val btnAcceptar = findViewById<Button>(R.id.btnAcceptar)
        val btnStyleNormal = findViewById<Button>(R.id.btnStyleNormal)
        val btnStyleBold = findViewById<Button>(R.id.btnStyleBold)
        val btnStyleItalic = findViewById<Button>(R.id.btnStyleItalic)
        val btnStyleBoldItalic = findViewById<Button>(R.id.btnStyleBoldItalic)
        val btnFontSize = findViewById<Button>(R.id.btnFontSize)
        txtText = findViewById(R.id.txtText)

        if(filePath !=null)
        {
            try
            {
                var text = MyFile.getText(filePath)
                txtText.setText(text)

                btnAcceptar.setOnClickListener()
                {
                    try
                    {
                        MyFile.saveText(txtText.text.toString(),filePath)
                        Toast.makeText(this, "Text actualitzat correctament", Toast.LENGTH_SHORT).show()
                    }
                    catch (e: java.lang.Exception)
                    {
                        Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show()
                    }
                }
                btnStyleBold.setOnClickListener()
                {
                    txtText.setTypeface(null, Typeface.BOLD)
                }
                btnStyleNormal.setOnClickListener()
                {
                    txtText.setTypeface(null, Typeface.NORMAL)
                }
                btnStyleItalic.setOnClickListener()
                {
                    txtText.setTypeface(null, Typeface.ITALIC)
                }
                btnStyleBoldItalic.setOnClickListener()
                {
                    txtText.setTypeface(null, Typeface.BOLD_ITALIC)
                }
                btnFontSize.setOnClickListener()
                {
                    showDialog()
                }
            }
            catch (e: IOException)
            {
                throw e
            }
        }
    }

    fun showDialog()
    {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_change_txtsize)

        val fontSeekBar = dialog.findViewById<SeekBar>(R.id.font_seekbar)
        val fontSizeTextView = dialog.findViewById<TextView>(R.id.font_size_textview)

        fontSeekBar.max = 48
        fontSeekBar.progress = txtText.textSize.toInt()

        fontSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txtText.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress.toFloat())
                fontSizeTextView.text = "$progress sp"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        dialog.show()

    }
}