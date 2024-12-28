package ru.kafpin.pr4

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // get the references from layout file
        val btnStartProgress: Button = findViewById(R.id.button1)
        val progressBar: ProgressBar = findViewById(R.id.progressBar1)

        // when button is clicked, start the task
        btnStartProgress.setOnClickListener { v ->

            // task is run on a thread
            Thread(Runnable {
                // dummy thread mimicking some operation whose progress cannot be tracked

                // display the indefinite progressbar
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    progressBar.visibility = View.VISIBLE
                })

                // performing some dummy time taking operation
                try {
                    var i=0;
                    while(i<Int.MAX_VALUE){
                        i++
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                // when the task is completed, make progressBar gone
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    progressBar.visibility = View.GONE
                })
            }).start()
        }


    }
}