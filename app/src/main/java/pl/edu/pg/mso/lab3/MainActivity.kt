package pl.edu.pg.mso.lab3

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listFiles(applicationContext.filesDir)
        val builder = AlertDialog.Builder(this)
        findViewById<Button>(R.id.privFiles)?.setOnClickListener {
            val fileName: String = findViewById<TextView>(R.id.fileName).text.toString()

            val dirName: String = findViewById<TextView>(R.id.dirName).text.toString()
            val subDir = File(applicationContext.filesDir, dirName)

            val helper = FilesHelper()
            val file = helper.preparePrivateFile(applicationContext, fileName, if (dirName != "Dir") dirName else null)
            val fileData = helper.readPrivateFile(file);

            findViewById<TextView>(R.id.fileContent).text = if (fileData != null) String(fileData) else ""
            builder.setMessage("file Opened")

            builder.create().show()
        }
        findViewById<Button>(R.id.saveBtn)?.setOnClickListener {
            val fileName: String = findViewById<TextView>(R.id.fileName).text.toString()
            val dirName: String = findViewById<TextView>(R.id.dirName).text.toString()
            val helper = FilesHelper()
            val file = helper.preparePrivateFile(applicationContext, fileName, if (dirName != "Dir") dirName else null)
            if(!file.exists())
                file.createNewFile()

            val fileData = findViewById<TextView>(R.id.fileContent).text.toString()
            helper.writeToPrivateFile(file, fileData.toByteArray(Charset.forName("UTF-8")))
            builder.setMessage("Saved")

            builder.create().show()
            listFiles(applicationContext.filesDir)
        }
    }
    private fun listFiles(dir: File): String {
        val files = dir.listFiles();


        var str = ""
        if(files.isNullOrEmpty()) {
            findViewById<TextView>(R.id.fileList).text = str
            return str
        }
        for (file in files) {
            str += file.toURI()
            str += '\n'
            if (file.isDirectory()) {
                val helper = FilesHelper()
                val pfile = helper.preparePrivateFile(applicationContext, "", file.name)
                str += listFiles(pfile)
            }
        }

        findViewById<TextView>(R.id.fileList).text = str
        return str
    }
}