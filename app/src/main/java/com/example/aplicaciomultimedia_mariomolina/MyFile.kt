package com.example.aplicaciomultimedia_mariomolina

import android.os.Environment
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class MyFile {

    companion object
    {

        fun getText(FILE_PATH: String): String
        {
            var s = ""
            try
            {
                val fr = FileReader(FILE_PATH)
                val br = BufferedReader(fr)
                var line = br.readLine()
                while (line != null)
                {
                    s+=line + "\n"
                    line = br.readLine()
                }
                fr.close()
            }
            catch (e: IOException)
            {
                throw e
            }
            return s
        }

        fun saveText(s: String,FILE_PATH: String)
        {
            try
            {
                val fw = FileWriter(FILE_PATH)
                fw.write(s)
                fw.close()
            }
            catch (e: IOException)
            {
                throw e
            }
        }

        fun createFile(FILE_PATH: String)
        {
            val file = File(FILE_PATH)
            if (!file.exists())
            {
                file.createNewFile()
            }
        }
    }

}
