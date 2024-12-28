package ru.kafpin.practice2

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast


import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ll_main = findViewById(R.id.ll_main_layout) as LinearLayout
        val options = arrayOf("Option 1", "Option 2", "Option 3", "Option 4")
        // create a radio group
        val rg = RadioGroup(this)
        rg.orientation = RadioGroup.VERTICAL
        for (i in options.indices) {
            // create a radio button
            val rb = RadioButton(this)
            // set text for the radio button
            rb.text = options[i]
            // assign an automatically generated id to the radio button
            rb.id = View.generateViewId()
            // add radio button to the radio group
            rg.addView(rb)
        }
        // add radio group to the linear layout
        ll_main.addView(rg)
    }

    }
}