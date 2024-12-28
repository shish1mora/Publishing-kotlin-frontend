package ru.kafpin.lr5db.dao

import android.icu.lang.UCharacter.GraphemeClusterBreak.T



interface Dao<T,ID> {
    fun findALl(): Collection<T?>?
    fun save(entity: T?): T?
    fun update(entity: T?): T?
    fun deleteById(id: ID?)
    fun findById(id: ID?): T?
}