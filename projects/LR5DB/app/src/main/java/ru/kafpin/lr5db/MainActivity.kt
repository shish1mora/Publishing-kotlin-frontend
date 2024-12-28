package ru.kafpin.lr5db

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.kafpin.lr5db.dao.ClientDao
import ru.kafpin.lr5db.domains.Client
import java.io.IOException
import java.util.Properties


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

//        resources.assets.openFd("client.properties").javaClass.getResource()

        val assetManager: AssetManager = resources.assets

        val clientDaoProperty : Properties = Properties()
        val dbHelperProperty : Properties = Properties()
        clientDaoProperty.load(resources.assets.open("client.properties"))
        dbHelperProperty.load(resources.assets.open("config.properties"))



        val clientDao : ClientDao = ClientDao(clientDaoProperty,dbHelperProperty)

        val lvClient : ListView = findViewById(R.id.lvClients)

        val clients : ArrayList<Client> = clientDao.findALl()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, clients
        )
        lvClient.adapter = adapter







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}