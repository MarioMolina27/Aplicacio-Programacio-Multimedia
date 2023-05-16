package com.example.aplicaciomultimedia_mariomolina.activites.intents

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.aplicaciomultimedia_mariomolina.R

class IntentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intents)

        val buttonMaps = findViewById<Button>(R.id.buttonMaps)
        val buttonWikipedia = findViewById<Button>(R.id.buttonWikipedia)
        val buttonTwitter = findViewById<Button>(R.id.buttonTwitter)

        buttonMaps.setOnClickListener()
        {
            showMapsAdressDialog()
        }

        buttonWikipedia.setOnClickListener()
        {
            showWikipediaDialog()
        }

        buttonTwitter.setOnClickListener()
        {
            showTwitterDialog()
        }
    }

    fun showMapsAdressDialog()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Introdueix una direcció")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Aceptar")
        {
            _,_ ->
            val address = input.text.toString()
            openGoogleMaps(address)
        }

        builder.setNegativeButton("Cancelar")
        {
                dialog,_ ->
            dialog.cancel()
        }

        builder.show()
    }

    fun showWikipediaDialog()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Introdueix un article a mostrar")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Aceptar")
        {
                _,_ ->
            val article = input.text.toString()
            openWikipediaArticle(article)
        }

        builder.setNegativeButton("Cancelar")
        {
                dialog,_ ->
            dialog.cancel()
        }

        builder.show()
    }

    fun showTwitterDialog()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Introdueix una busqueda a Twitter")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Aceptar")
        {
                _,_ ->
            val article = input.text.toString()
            searchOnTwitter(article)
        }

        builder.setNegativeButton("Cancelar")
        {
                dialog,_ ->
            dialog.cancel()
        }

        builder.show()
    }

    fun openGoogleMaps(address: String)
    {
        val uri =Uri.parse("geo:0,0?q=$address")
        val intent = Intent(Intent.ACTION_VIEW,uri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    fun openWikipediaArticle(article: String)
    {
        val uri = Uri.parse("https://es.wikipedia.org/wiki/$article")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun searchOnTwitter(query: String)
    {
        val encodedQuery = Uri.encode(query)
        val twitterUri = Uri.parse("https://twitter.com/search?q=$encodedQuery&src=typed_query")
        val intent = Intent(Intent.ACTION_VIEW, twitterUri)
        intent.setPackage("com.twitter.android")


        val packageManager = packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        val isTwitterInstalled = activities.size > 0

        if (isTwitterInstalled)
        {
            startActivity(intent)
        }
        else
        {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/search?q=$encodedQuery"))
            startActivity(browserIntent)
        }
    }


}