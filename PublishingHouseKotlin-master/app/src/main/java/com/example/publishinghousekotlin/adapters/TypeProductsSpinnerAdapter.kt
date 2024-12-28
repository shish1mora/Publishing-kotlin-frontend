package com.example.publishinghousekotlin.adapters

import android.R
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.publishinghousekotlin.models.TypeProduct

/**
 * Адаптер для отображения элементов типа [TypeProduct] в [Spinner].
 *
 * Этот адаптер расширяет базовый класс [ArrayAdapter] и предоставляет
 * специфичные методы для работы с элементами типа [TypeProduct].
 *
 * @author Денис
 * @since 1.0.0
 * @property context Контекст, в котором используется адаптер.
 * @property typeProducts Список объектов [TypeProduct], которые будут отображаться в адаптере.
 */
class TypeProductsSpinnerAdapter(context: Context, typeProducts: List<TypeProduct>): ArrayAdapter<TypeProduct>(context, R.layout.simple_spinner_dropdown_item, typeProducts) {

    /**
     * Получение отображаемого представления элемента в [Spinner].
     *
     * @param position Позиция элемента в списке.
     * @param convertView Представление элемента, которое может быть переиспользовано.
     * @param parent Родительский контейнер, к которому привязан элемент.
     * @return Визуальное представление элемента в [Spinner].
     */
    override fun getView(position: Int, convertView: View?, parent:ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val typeProduct = getItem(position)
        (view as TextView).text = typeProduct?.type
        return view
    }

    /**
     * Получение отображаемого представления выпадающего списка элемента в [Spinner].
     *
     * @param position Позиция элемента в списке.
     * @param convertView Представление элемента, которое может быть переиспользовано.
     * @param parent Родительский контейнер, к которому привязан элемент в выпадающем списке.
     * @return Визуальное представление элемента в выпадающем списке [Spinner].
     */
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val typeProduct = getItem(position)
        (view as TextView).text = typeProduct?.type
        return view
    }

    /**
     * Получение объекта [TypeProduct] по его позиции в списке.
     *
     * @param position Позиция элемента в списке.
     * @return Объект [TypeProduct], соответствующий указанной позиции.
     */
    fun getSelectedTypeProduct(position: Int):TypeProduct?{
        return getItem(position)
    }
}