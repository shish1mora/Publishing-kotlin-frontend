package com.example.publishinghousekotlin.basics

/**
 * Интерфейс для обработки события нажатия на элемент в списке.
 * @author Денис
 * @since 1.0.0
 */
interface OnItemClickListener {

    /**
     * Метод,вызываемый при нажатии на элемент списка.
     *
     * @param position Позиция элемента, на который произошло нажатие.
     */
    fun onItemClick(position:Int)
}