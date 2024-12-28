package ru.kafpin.lr2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


// Константа для идентификатора запроса выбора вора
const val REQUEST_CHOOSE_THIEF = 0

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Вызов метода родительского класса для корректной инициализации

        setContentView(R.layout.activity_main) // Установка макета активности из XML-файла

        // Поиск кнопки по её идентификатору из макета
        val button_choose: Button = findViewById(R.id.button_choose)

        // Установка слушателя нажатий для кнопки
        button_choose.setOnClickListener {
            // Создание намерения для перехода на SecondActivity
            val questionIntent = Intent(this@MainActivity, SecondActivity::class.java)
            // Запуск SecondActivity и ожидание результата
            startActivityForResult(questionIntent, REQUEST_CHOOSE_THIEF)
        }
    }

    // Обработка результата, возвращаемого из SecondActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data) // Вызов метода родительского класса

        // Поиск текстового представления по его идентификатору из макета
        val textview_info: TextView = findViewById(R.id.textview_into)

        // Проверка, был ли результат успешным
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CHOOSE_THIEF -> {
                    // Извлечение имени вора из возвращаемых данных
                    val thiefName = data?.getStringExtra(THIEF)
                    // Установка имени вора в текстовое представление
                    textview_info.text = thiefName.toString()
                }
            }
        } else {
            // Очистка текста, если результат не успешен
            textview_info.text = "" // Стираем текст
        }
    }
}

