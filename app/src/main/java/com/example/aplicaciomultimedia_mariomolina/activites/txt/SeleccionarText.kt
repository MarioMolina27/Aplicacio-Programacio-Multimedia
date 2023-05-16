package com.example.aplicaciomultimedia_mariomolina.activites.txt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.aplicaciomultimedia_mariomolina.Keys
import com.example.aplicaciomultimedia_mariomolina.MyFile
import com.example.aplicaciomultimedia_mariomolina.R
import com.example.aplicaciomultimedia_mariomolina.adapters.FilesAdapter
import java.io.File

class SeleccionarText : AppCompatActivity() {

    private lateinit var adapter: FilesAdapter
    private lateinit var files: ArrayList<File>
    private lateinit var downloadPath: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_text)

        val DOWNLOAD_PATH: String = Environment.getExternalStorageDirectory().path + File.separator + Environment.DIRECTORY_DOWNLOADS
        downloadPath = File(DOWNLOAD_PATH)
        val filesList = downloadPath.listFiles()?.toList() ?: emptyList()
        val txtFilesList = filesList.filter { it.name.endsWith(".txt") }
        files = ArrayList<File>(txtFilesList)
        adapter = FilesAdapter(this,R.layout.txt_item_select,files)
        if(files !=null&& files.isNotEmpty())
        {
            val lstArxius = findViewById<ListView>(R.id.lstArxius)
            lstArxius.adapter = adapter

            lstArxius.onItemClickListener = AdapterView.OnItemClickListener(){
                    _,_,i,_ ->

                val intent = Intent(this, TextActivity::class.java)
                intent.putExtra(Keys.constKeys.SELECT_FILE,files[i].absolutePath)
                startActivity(intent)
            }
        }

        val btnNewTxt = findViewById<Button>(R.id.btnNewTxt)

        btnNewTxt.setOnClickListener()
        {
            showDialogNewText()
        }
    }

    fun showDialogNewText()
    {
        val myDialogView = LayoutInflater.from(this).inflate(R.layout.nom_arxiu_dialog,null)

        val myBuilder = AlertDialog.Builder(this)
            .setView(myDialogView)
        val myAlertDialog = myBuilder.show()
        myAlertDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        val btnGuardar =myDialogView.findViewById<Button>(R.id.btnGuardarArxiu)
        val editText =myDialogView.findViewById<EditText>(R.id.dialogNouArxiu)

        btnGuardar.setOnClickListener()
        {
            if(!editText.text.toString().equals(""))
            {
                val DOWNLOAD_PATH: String = Environment.getExternalStorageDirectory().path + File.separator + Environment.DIRECTORY_DOWNLOADS
                val nomArixu = editText.text.toString() + ".txt"
                val arxiu = DOWNLOAD_PATH+"/" + nomArixu
                val file = File(arxiu)
                if (!file.exists())
                {
                    files.add(file)
                }
                MyFile.createFile(arxiu)
                adapter.notifyDataSetChanged()
            }
            myAlertDialog.dismiss()
        }
    }
}