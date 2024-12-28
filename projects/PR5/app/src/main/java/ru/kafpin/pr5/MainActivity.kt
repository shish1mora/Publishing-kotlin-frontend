package ru.kafpin.pr5

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
class MainActivity : AppCompatActivity() {
    // Метод onCreate вызывается при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Вызов метода родительского класса для корректной инициализации
        enableEdgeToEdge() // Включение режима "от края до края" для пользовательского интерфейса
        setContentView(R.layout.activity_main) // Установка макета активности из XML-файла

        // Создание массива строк, представляющего названия городов
        var array = arrayOf("Melbourne", "Vienna", "Vancouver", "Toronto", "Calgary",
            "Adelaide", "Perth", "Auckland", "Helsinki", "Hamburg",
            "Munich", "New York", "Sydney", "Paris", "Cape Town",
            "Barcelona", "London", "Bangkok")

        // Создание адаптера для ListView с использованием массива городов
        val adapter = ArrayAdapter(this, R.layout.listview_item, array)

        // Поиск ListView по его идентификатору из макета
        val listView: ListView = findViewById(R.id.listview_1)
        listView.setAdapter(adapter) // Установка адаптера для ListView

        // Установка слушателя нажатий для элементов списка
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            // Метод, который вызывается при нажатии на элемент списка
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Получение значения элемента, на который кликнули
                val itemValue = listView.getItemAtPosition(position) as String

                // Отображение всплывающего сообщения (Toast) с позицией и значением элемента
                Toast.makeText(applicationContext, "Position :$position\nItem Value : $itemValue", Toast.LENGTH_LONG).show()
            }
        }

        // Поиск кнопки изменения по её идентификатору
        val change_btn = findViewById(R.id.change) as Button
        change_btn.setOnClickListener {
            // Изменение данных в исходном массиве
            array.set(0, "New City") // Изменение первого элемента массива
            array.set(1, "Another New City") // Изменение второго элемента массива
        }

        // Поиск кнопки обновления по её идентификатору
        val btn_click_me = findViewById(R.id.refresh) as Button
        btn_click_me.setOnClickListener {
            // Уведомление адаптера о том, что данные изменились
            // Адаптер уведомит соответствующие представления, и они обновятся
            adapter.notifyDataSetChanged()
        }
    }
}

Найти еще