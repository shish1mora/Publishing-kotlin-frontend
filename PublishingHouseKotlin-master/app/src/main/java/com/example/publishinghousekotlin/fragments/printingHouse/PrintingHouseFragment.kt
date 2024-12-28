package com.example.publishinghousekotlin.fragments.printingHouse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.publishinghousekotlin.adapters.PrintingHouseAdapter
import com.example.publishinghousekotlin.basics.OnItemClickListener
import com.example.publishinghousekotlin.controllers.DetailsPrintingHouseActivity
import com.example.publishinghousekotlin.databinding.FragmentGeneralBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

/**
 * Fragment для отображения списка типографий
 * @author Климачков Даниил
 * @since 1.0.0
 * @property _fragmentPrintingHousesBinding Биндинг для доступа к компонентам
 * @property fragmentPrintingHousesBinding Инициализированный биндинг
 * @property printingHouseViewModel ViewModel типографий
 * @property adapter Adapter для recyclerView
 */
class PrintingHouseFragment: Fragment(), OnItemClickListener {

    private var _fragmentPrintingHousesBinding: FragmentGeneralBinding? = null
    private val fragmentPrintingHousesBinding get() = _fragmentPrintingHousesBinding!!

    private lateinit var printingHouseViewModel: PrintingHouseViewModel
    private lateinit var adapter: PrintingHouseAdapter


    /**
     * Метод,вызываемый при создании и отображении макета фрагмента.
     *
     * @param inflater Объект LayoutInflater, который может быть использован для
     * создания представления фрагмента.
     * @param container Если непустой, это корневое представление,
     * к которому будет прикреплено фрагмент.
     * @param savedInstanceState Если не нулевой, то в этом объекте содержится
     * состояние фрагмента, сохраненное в последнем вызове onSaveInstanceState().
     * @return Возвращает созданное представление фрагмента.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        printingHouseViewModel = ViewModelProvider(this)[PrintingHouseViewModel::class.java]

        _fragmentPrintingHousesBinding = FragmentGeneralBinding.inflate(inflater,container,false)
        fragmentPrintingHousesBinding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        fragmentPrintingHousesBinding.searchTextInputLayout.hint = "Поиск по названию"

        adapter = PrintingHouseAdapter(this)
        fragmentPrintingHousesBinding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        //fragmentPrintingHousesBinding.recyclerView.setHasFixedSize(true)
        fragmentPrintingHousesBinding.recyclerView.adapter = adapter

        printingHouseViewModel.listOfPrintingHouses.observe(this.viewLifecycleOwner){
            adapter.submitData(lifecycle,it)
        }

        fragmentPrintingHousesBinding.searchEditText.addTextChangedListener {
            printingHouseViewModel.updateSearchType(fragmentPrintingHousesBinding.searchEditText.text.toString().trim())
        }


        return fragmentPrintingHousesBinding.root
    }


    /**
     * Метод, вызываемый при уничтожении представления фрагмента.
     * В данном случае, освобождает ресурсы, связанные с ViewBinding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentPrintingHousesBinding = null
    }


    /**
     * Метод, вызываемый при клике на элемент списка типографий.
     * Открывает экран с детальной информацией о выбранной типографии.
     *
     * @param position Позиция выбранного элемента в списке.
     */
    override fun onItemClick(position: Int) {

        val intent = Intent(activity, DetailsPrintingHouseActivity::class.java)
        intent.putExtra("printingHouse", adapter.getPrintingHouse(position))
        startActivity(intent)
    }
}