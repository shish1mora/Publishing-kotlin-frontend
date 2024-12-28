package com.example.publishinghousekotlin.fragments.booking

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
import com.example.publishinghousekotlin.adapters.BookingsAdapter
import com.example.publishinghousekotlin.basics.OnItemClickListener
import com.example.publishinghousekotlin.controllers.DetailsBookingActivity
import com.example.publishinghousekotlin.controllers.GenerateReportBookingActivity
import com.example.publishinghousekotlin.databinding.FragmentBookingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class  BookingFragment : Fragment(), OnItemClickListener {

    private var _fragmentBookingBinding: FragmentBookingBinding? = null
    private val fragmentBookingBinding get() = _fragmentBookingBinding!!

    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var adapter: BookingsAdapter

    private var statusSearching = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bookingViewModel = ViewModelProvider(this)[BookingViewModel::class.java]

        _fragmentBookingBinding = FragmentBookingBinding.inflate(inflater,container,false)
        fragmentBookingBinding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        fragmentBookingBinding.searchTextInputLayout.hint = "Поиск по статусу"

        adapter = BookingsAdapter(this)
        fragmentBookingBinding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        //fragmentBookingBinding.recyclerView.setHasFixedSize(true)
        fragmentBookingBinding.recyclerView.adapter = adapter


        bookingViewModel.listOfBookings.observe(this.viewLifecycleOwner){
            adapter.submitData(lifecycle,it)
        }


        fragmentBookingBinding.searchEditText.addTextChangedListener {
            if(statusSearching){
                bookingViewModel.updateSearchParameters(null, fragmentBookingBinding.searchEditText.text.toString().trim())
            }else{
                val text = fragmentBookingBinding.searchEditText.text.toString().trim()
                if(text != "") {
                    bookingViewModel.updateSearchParameters(text.toLong(), "")
                }else{
                    bookingViewModel.updateSearchParameters(null, "")
                }
            }
        }



        fragmentBookingBinding.generateReportBtn.setOnClickListener {
            val intent = Intent(activity, GenerateReportBookingActivity::class.java)
            startActivity(intent)

        }


        return fragmentBookingBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBookingBinding = null
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, DetailsBookingActivity::class.java)
        intent.putExtra("bookingId", adapter.getBookingSimpleAcceptDTO(position)!!.id)
        startActivity(intent)
    }


}