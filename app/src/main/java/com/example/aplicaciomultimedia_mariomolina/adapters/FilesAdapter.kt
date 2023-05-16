package com.example.aplicaciomultimedia_mariomolina.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.aplicaciomultimedia_mariomolina.R
import java.io.File

class FilesAdapter(context: Context, val layout: Int, val files: ArrayList<File>):
    ArrayAdapter<File>(context, layout, files)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView !=null){
            view = convertView
        }
        else{
            view =
                LayoutInflater.from(getContext()).inflate(layout, parent, false)
        }
        bindFile(view,files[position])
        return view
    }

    fun bindFile(view: View, file: File)
    {
        val nomArxiuLst = view.findViewById<TextView>(R.id.nomArxiuLst)
        nomArxiuLst.text= file.name
    }
}