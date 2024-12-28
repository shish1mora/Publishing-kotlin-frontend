package com.example.publishinghousekotlin.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.publishinghousekotlin.models.PrintingHouse

class PrintingHousesSpinnerAdapter(context: Context, printingHouses: List<PrintingHouse>): ArrayAdapter<PrintingHouse>(context, android.R.layout.simple_spinner_dropdown_item, printingHouses) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val printingHouse = getItem(position)
        (view as TextView).text = printingHouse?.name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val printingHouse = getItem(position)
        (view as TextView).text = printingHouse?.name
        return view
    }

    fun getSelectedPrintingHouse(position: Int):PrintingHouse?{
        return getItem(position)
    }
}