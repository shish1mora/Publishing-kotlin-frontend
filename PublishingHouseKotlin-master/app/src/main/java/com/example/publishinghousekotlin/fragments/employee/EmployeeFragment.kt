package com.example.publishinghousekotlin.fragments.employee

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
import com.example.publishinghousekotlin.adapters.EmployeesAdapter
import com.example.publishinghousekotlin.basics.OnItemClickListener
import com.example.publishinghousekotlin.controllers.DetailsEmployeeActivity
import com.example.publishinghousekotlin.databinding.FragmentGeneralBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

/**
 * Fragment для отображения списка сотрудников
 * @author Климачков Даниил
 * @since 1.0.0
 * @property _fragmentEmployeesBinding Биндинг для доступа к компонентам
 * @property fragmentEmployeesBinding Инициализированный биндинг
 * @property employeeViewModel ViewModel сотрудников
 * @property adapter Adapter для recyclerView
 */
class EmployeeFragment: Fragment(), OnItemClickListener {

    private var _fragmentEmployeesBinding: FragmentGeneralBinding? = null
    private val fragmentEmployeesBinding get() = _fragmentEmployeesBinding!!

    private lateinit var employeeViewModel: EmployeeViewModel

    private lateinit var adapter: EmployeesAdapter


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
        employeeViewModel = ViewModelProvider(this)[EmployeeViewModel::class.java]

        _fragmentEmployeesBinding = FragmentGeneralBinding.inflate(inflater,container, false)
        fragmentEmployeesBinding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        fragmentEmployeesBinding.searchTextInputLayout.hint = "Поиск по фамилии"

        adapter = EmployeesAdapter(this)
        fragmentEmployeesBinding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        fragmentEmployeesBinding.recyclerView.adapter = adapter

        employeeViewModel.listOfEmployees.observe(this.viewLifecycleOwner){
            adapter.submitData(lifecycle,it)
        }

        fragmentEmployeesBinding.searchEditText.addTextChangedListener {
            employeeViewModel.updateSearchType(fragmentEmployeesBinding.searchEditText.text.toString().trim())
        }


        return fragmentEmployeesBinding.root
    }

    /**
     * Метод, вызываемый при уничтожении представления фрагмента.
     * В данном случае, освобождает ресурсы, связанные с ViewBinding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentEmployeesBinding = null
    }

    /**
     * Метод, вызываемый при клике на элемент списка сотрудников.
     * Открывает экран с детальной информацией о выбранном сотруднике.
     *
     * @param position Позиция выбранного элемента в списке.
     */
    override fun onItemClick(position: Int) {
        val intent = Intent(activity, DetailsEmployeeActivity::class.java)
        intent.putExtra("employee", adapter.getEmployeeDTO(position))
        startActivity(intent)
    }
}