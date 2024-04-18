package pl.edu.pg.mso.lab3

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
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

        listFiles()
        val builder = AlertDialog.Builder(this)
        findViewById<Button>(R.id.privFiles)?.setOnClickListener {
            val fileName: String = findViewById<TextView>(R.id.fileName).text.toString()
            val helper = FilesHelper()
            val file = helper.preparePrivateFile(applicationContext, fileName)
            val fileData = helper.readPrivateFile(file);

            findViewById<TextView>(R.id.fileContent).text = if (fileData != null) String(fileData) else ""
            builder.setMessage("file Opened")

            builder.create().show()
        }
        findViewById<Button>(R.id.saveBtn)?.setOnClickListener {
            val fileName: String = findViewById<TextView>(R.id.fileName).text.toString()
            val helper = FilesHelper()
            val file = helper.preparePrivateFile(applicationContext, fileName)

            val fileData = findViewById<TextView>(R.id.fileContent).text.toString()
            helper.writeToPrivateFile(file, fileData.toByteArray(Charset.forName("UTF-8")))
            builder.setMessage("Saved")

            builder.create().show()
            listFiles()
        }
    }
    private fun listFiles() {
        val dir = applicationContext.filesDir
        val files = dir.listFiles();

        var str = ""
        if(files.isNullOrEmpty()) {
            findViewById<TextView>(R.id.fileList).text = str
            return
        }
        for (file in files) {
            str += file.toURI()
            str += '\n'
        }
        findViewById<TextView>(R.id.fileList).text = str
    }
}