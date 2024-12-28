package ru.kafpin.pr2

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btn : Button = findViewById(R.id.button)
        btn.setOnClickListener{
            // Создаем Intent для перехода на новый экран (замените на ваш класс и экран)
            val intent: Intent = Intent(this@MainActivity,SecondActivity::class.java)
            // Запускаем новый экран
            startActivity(intent)
        }

    }
}