package com.example.publishinghousekotlin

import android.app.Application

/**
 * Класс-приложение, представляющий собой точку входа в приложение и обеспечивающий
 * доступ к контексту приложения из любой точки приложения.
 */
class MyApplication: Application() {

    companion object {
        /**
         * Статическое свойство, представляющее собой экземпляр текущего приложения.
         */
        lateinit var instance: MyApplication
            private set
    }

    /**
     * Метод, вызываемый при создании приложения.
     */
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}