package ru.kafpin.lr5db.domains

class Client {
    private var clientID: Long? = null
    private var name: String? = null

    /** Поле фамилия */
    private var surName: String? = null

    /** Поле отчество */
    private var patronym: String? = null

    /** Поле номер телефона */
    private var phone: String? = null

    constructor(
        clientID: Long?,
        surName: String?,
        name: String?,
        patronym: String?,
        phone: String?
    ) {
        this.clientID = clientID
        this.name = name
        this.surName = surName
        this.patronym = patronym
        this.phone = phone
    }

    override fun toString(): String {
        return "$clientID $name $surName $patronym \n" +
                "$phone)"
    }
}