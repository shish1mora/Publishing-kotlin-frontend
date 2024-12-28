package ru.kafpin.lr5

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.sql.SQLException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val button:Button = findViewById(R.id.button)
        val textView: TextView = findViewById(R.id.textView)
        val mDBHelper: DatabaseHelper
        val mDb : SQLiteDatabase

        mDBHelper = DatabaseHelper(this)

        try {
            mDBHelper.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }

        try {
            mDb = mDBHelper.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }


        button.setOnClickListener {
            val query = "INSERT INTO clients (name, age) VALUES ('Tom', '12')"
            mDb.execSQL(query)
            updateList(mDb)
        }
        updateList(mDb)
    }
    fun updateList(mDb : SQLiteDatabase)
    {
        // Список клиентов
        val clients = ArrayList<HashMap<String, Any?>>()
        // Список параметров конкретного клиента
        var client: HashMap<String, Any?>

        // Отправляем запрос в БД
        val cursor: Cursor = mDb.rawQuery("SELECT * FROM clients", null)
        cursor.moveToFirst()

        // Пробегаем по всем клиентам
        while (!cursor.isAfterLast) {
            client = HashMap()

            // Заполняем клиента
            client["name"] = cursor.getString(1)
            client["age"] = cursor.getString(2)

            // Закидываем клиента в список клиентов
            clients.add(client)

            // Переходим к следующему клиенту
            cursor.moveToNext()
        }
        cursor.close()


        // Какие параметры клиента мы будем отображать в соответствующих
        // элементах из разметки adapter_item.xml
        val from = arrayOf("name", "age")
        val to = intArrayOf(R.id.textView, R.id.textView2)


        // Создаем адаптер
        val adapter = SimpleAdapter(this, clients, R.layout.adapter_item, from, to)
        val listView = findViewById<View>(R.id.listView) as ListView
        listView.adapter = adapter

    }
}