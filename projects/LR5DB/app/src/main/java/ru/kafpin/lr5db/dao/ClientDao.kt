package ru.kafpin.lr5db.dao

import android.content.res.AssetManager
import ru.kafpin.lr5db.domains.Client
import ru.kafpin.lr5db.utils.DBHelper
import java.io.IOException
import java.io.InputStream
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Properties


class ClientDao(private var clientDaoProperties: Properties, dbHelperProperties: Properties) : Dao<Client,Long> {


    private var dbHelper:DBHelper = DBHelper(dbHelperProperties)


    override fun findALl(): ArrayList<Client> {
        var list: ArrayList<Client>
        var rs: ResultSet? = null

        val statement : PreparedStatement = dbHelper.getConnection()!!.prepareStatement(clientDaoProperties.getProperty("sql.select"))
        rs = statement.executeQuery();
        list = mapper(rs);
        return list
    }

    override fun save(entity: Client?): Client? {
        TODO("Not yet implemented")
    }

    override fun update(entity: Client?): Client? {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long?) {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long?): Client? {
        TODO("Not yet implemented")
    }
    private fun mapper(rs: ResultSet): ArrayList<Client> {
        val list: ArrayList<Client> = ArrayList()
        try {
            while (rs.next()) {
                list.add(
                    Client(
                        rs.getLong("id"),
                        rs.getString("surname"),
                        rs.getString("name"),
                        rs.getString("patronym"),
                        rs.getString("phone")
                    )
                )
            }
        }
        catch (e: SQLException) {
            println(e.message)
        }
        return list
    }
}