package com.example.publishinghousekotlin.fragments.material

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
import com.example.publishinghousekotlin.adapters.MaterialsAdapter
import com.example.publishinghousekotlin.basics.OnItemClickListener
import com.example.publishinghousekotlin.controllers.DetailsMaterialActivity
import com.example.publishinghousekotlin.databinding.FragmentGeneralBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
/**
 * Fragment для отображения списка материалов
 * @author Климачков Даниил
 * @since 1.0.0
 * @property _fragmentMaterialsBinding Биндинг для доступа к компонентам
 * @property fragmentMaterialsBinding Инициализированный биндинг
 * @property materialViewModel ViewModel материалов
 * @property adapter Adapter для recyclerView
 */
class MaterialFragment : Fragment(), OnItemClickListener {

    private var _fragmentMaterialsBinding: FragmentGeneralBinding? = null
    private val fragmentMaterialsBinding get() = _fragmentMaterialsBinding!!

    private lateinit var materialViewModel: MaterialViewModel
    private lateinit var adapter: MaterialsAdapter


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
        materialViewModel = ViewModelProvider(this)[MaterialViewModel::class.java]

        _fragmentMaterialsBinding = FragmentGeneralBinding.inflate(inflater, container, false)
        fragmentMaterialsBinding.recyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        fragmentMaterialsBinding.searchTextInputLayout.hint = "Поиск по типу"

        adapter = MaterialsAdapter(this)
        fragmentMaterialsBinding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        //fragmentMaterialsBinding.recyclerView.setHasFixedSize(true)
        fragmentMaterialsBinding.recyclerView.adapter = adapter

        materialViewModel.listOfMaterials.observe(this.viewLifecycleOwner){
            adapter.submitData(lifecycle, it)
        }

        fragmentMaterialsBinding.searchEditText.addTextChangedListener {
            materialViewModel.updateSearchType(fragmentMaterialsBinding.searchEditText.text.toString().trim())
        }


        return fragmentMaterialsBinding.root
    }

    /**
     * Метод, вызываемый при уничтожении представления фрагмента.
     * В данном случае, освобождает ресурсы, связанные с ViewBinding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentMaterialsBinding = null
    }

    /**
     * Метод, вызываемый при клике на элемент списка материалов.
     * Открывает экран с детальной информацией о выбранном материале.
     *
     * @param position Позиция выбранного элемента в списке.
     */
    override fun onItemClick(position: Int) {
        val intent = Intent(activity, DetailsMaterialActivity::class.java)
        intent.putExtra("material", adapter.getMaterial(position))
        startActivity(intent)
    }
}