package com.example.publishinghousekotlin.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Adapter, отвечающий за сериализацию и десериализацию LocalDate
 * @author Денис
 * @since 1.0.0
 */
class LocalDateAdapter: JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    /**
     * Метод сериализации объекта [LocalDate] в формате ISO_LOCAL_DATE.
     *
     * Этот метод преобразует объект [LocalDate] в его строковое представление
     * в формате ISO_LOCAL_DATE и возвращает его в виде объекта [JsonPrimitive].
     *
     * @param localData Объект [LocalDate] для сериализации.
     * @param typeOfDate Тип даты (используется в GSON, может быть null).
     * @param context Контекст сериализации JSON (используется в GSON, может быть null).
     * @return Объект [JsonPrimitive] с сериализованным представлением [LocalDate].
     */
    override fun serialize(localData: LocalDate, typeOfDate: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(localData.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }


    /**
     * Метод десериализации объекта [LocalDate] из строки в формате ISO_LOCAL_DATE.
     *
     * Этот метод принимает JSON-элемент в виде строки, содержащей дату
     * в формате ISO_LOCAL_DATE, и преобразует его в объект [LocalDate].
     *
     * @param json JSON-элемент с датой в формате ISO_LOCAL_DATE.
     * @param typeOfT Тип объекта (используется в GSON, может быть null).
     * @param context Контекст десериализации JSON (используется в GSON, может быть null).
     * @return Десериализованный объект [LocalDate].
     */
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
        return LocalDate.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE)
    }

}