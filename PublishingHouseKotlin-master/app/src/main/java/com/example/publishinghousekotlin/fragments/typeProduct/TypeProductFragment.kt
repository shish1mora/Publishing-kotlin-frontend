package com.example.publishinghousekotlin.fragments.typeProduct

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
import com.example.publishinghousekotlin.adapters.TypeProductsAdapter
import com.example.publishinghousekotlin.basics.OnItemClickListener
import com.example.publishinghousekotlin.controllers.DetailsTypeProductActivity
import com.example.publishinghousekotlin.databinding.FragmentGeneralBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

/**
 * Fragment для отображения списка типов продукции
 * @author Климачков Даниил
 * @since 1.0.0
 * @property _fragmentTypeProductsBinding Биндинг для доступа к компонентам
 * @property fragmentTypeProductsBinding Инициализированный биндинг
 * @property typeProductViewModel ViewModel типов продукции
 * @property adapter Adapter для recyclerView
 */
class TypeProductFragment : Fragment(), OnItemClickListener {

    private var _fragmentTypeProductsBinding: FragmentGeneralBinding? = null
    private val fragmentTypeProductsBinding get() = _fragmentTypeProductsBinding!!

    private lateinit var typeProductViewModel: TypeProductViewModel
    private lateinit var adapter: TypeProductsAdapter


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

        typeProductViewModel = ViewModelProvider(this)[TypeProductViewModel::class.java]
        _fragmentTypeProductsBinding = FragmentGeneralBinding.inflate(inflater, container, false)
        fragmentTypeProductsBinding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        fragmentTypeProductsBinding.searchTextInputLayout.hint = "Поиск по типу"

        adapter = TypeProductsAdapter(this)
        fragmentTypeProductsBinding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        //fragmentTypeProductsBinding.recyclerView.setHasFixedSize(true)
        fragmentTypeProductsBinding.recyclerView.adapter = adapter

        typeProductViewModel.listOfTypeProducts.observe(this.viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }

        fragmentTypeProductsBinding.searchEditText.addTextChangedListener {
            typeProductViewModel.updateSearchType(fragmentTypeProductsBinding.searchEditText.text.toString().trim())
        }


        return fragmentTypeProductsBinding.root
    }


    /**
     * Метод, вызываемый при уничтожении представления фрагмента.
     * В данном случае, освобождает ресурсы, связанные с ViewBinding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentTypeProductsBinding = null
    }

    /**
     * Метод, вызываемый при клике на элемент списка типов продукции.
     * Открывает экран с детальной информацией о выбранном типе продукции.
     *
     * @param position Позиция выбранного элемента в списке.
     */
    override fun onItemClick(position:Int) {

        val intent = Intent(activity, DetailsTypeProductActivity::class.java)
        intent.putExtra("typeProduct", adapter.getTypeProduct(position))
        startActivity(intent)
    }
}