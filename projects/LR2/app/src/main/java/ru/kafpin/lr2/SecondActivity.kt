package ru.kafpin.lr2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import ru.kafpin.lr2.databinding.ActivitySecondBinding

const val THIEF = "ru.kafpin.lr2.THIEF"
class SecondActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)

        val radioGroup : RadioGroup = findViewById(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener { _, optionId ->
            val answerIntent = Intent()
            when (optionId) {
                R.id.radio_dog -> answerIntent.putExtra(THIEF, "Сраный пёсик")
                R.id.radio_crow -> answerIntent.putExtra(THIEF, "Ворона")
                R.id.radio_cat -> answerIntent.putExtra(THIEF, "Лошадь Пржевальского")
            }

            setResult(RESULT_OK, answerIntent)
            finish()
        }


    }
}