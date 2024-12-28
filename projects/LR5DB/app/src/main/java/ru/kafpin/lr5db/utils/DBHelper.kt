package ru.kafpin.lr5db.utils

import android.content.res.AssetManager
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties

class DBHelper(private val dbHelperProperty: Properties) {
    private var connection: Connection? = null


    @Throws(SQLException::class)
    fun getConnection(): Connection? {
        if (connection != null) {
            return connection
        }

        val dbUrl = dbHelperProperty.getProperty("db.url")
        val user = dbHelperProperty.getProperty("db.user")
        val pass = dbHelperProperty.getProperty("db.password")
        connection = DriverManager.getConnection(dbUrl, user, pass)
        return connection
    }
}