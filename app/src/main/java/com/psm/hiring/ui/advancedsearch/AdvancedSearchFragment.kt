package com.psm.hiring.ui.advancedsearch

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.hiring.General.OnRecyclerViewActionsListener_AdvanceSearch
import com.psm.hiring.General.OnRecyclerViewActionsListener_Home
import com.psm.hiring.R
import com.psm.hiring.adapters.JobAdapter
import com.psm.hiring.databinding.FragmentAdvancedSearchBinding
import com.psm.hiring.ui.home.HomeFragment
import com.psm.hiring.ui.home.HomeViewModel
import java.math.BigInteger
import java.util.*

import androidx.lifecycle.Observer

class AdvancedSearchFragment : Fragment(), OnRecyclerViewActionsListener_AdvanceSearch , AdapterView.OnItemSelectedListener{

    companion object {
        fun newInstance() = AdvancedSearchFragment()
        const val QUERY_PAGE_SIZE = 5
    }

    private var _binding: FragmentAdvancedSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var AdvancedViewModel: HomeViewModel
    private lateinit var jobsAdapter: JobAdapter
    private lateinit var recyclerView : RecyclerView

    private lateinit var et_TitleJob : EditText
    private lateinit var et_dateFrom : TextView
    private lateinit var et_dateTo : TextView
    private lateinit var et_paymentFrom : TextView
    private lateinit var et_paymentTo : TextView
    private lateinit var todays_date : String
    private lateinit var calendar : Calendar
    private lateinit var spinner : Spinner;


    private var listenerAdvanceSearch: OnRecyclerViewActionsListener_AdvanceSearch? = null

    var FiltroBusqueda:String? = "PagoDescendente"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdvancedSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        et_TitleJob = binding.etSearch
        et_dateFrom = binding.etDateFrom
        et_dateTo = binding.etDateTo
        et_paymentFrom = binding.etPaymentFrom
        et_paymentTo = binding.etPaymentTo

        calendar = Calendar.getInstance()

        /*val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        todays_date = "$year-$month-$day"

        et_dateFrom.text = "1900-01-01"
        et_dateTo.text = todays_date*/

        this.spinner = binding.sQuery

        val queries = resources.getStringArray(R.array.queries)

        val adapter = context?.let { ArrayAdapter(it, R.layout.spinner_style, queries) }
        this.spinner.adapter = adapter

        spinner.onItemSelectedListener  = this

        et_dateFrom.setOnClickListener {
            showDatePickerDialog(et_dateFrom)
        }

        et_dateTo.setOnClickListener {
            showDatePickerDialog(et_dateTo)
        }


        val btnAdvanceSearch = root?.findViewById<ImageButton>(R.id.btn_lookingForSearch)
        btnAdvanceSearch.setOnClickListener{
            BusquedaAvanzada()
        }




        this.recyclerView = binding.rvJobs
        this.recyclerView.addOnScrollListener(this@AdvancedSearchFragment.scrollListener)

        AdvancedViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        AdvancedViewModel.busquedaAvanzada = true
        BusquedaAvanzada()

        setupRecyclerView()

        AdvancedViewModel.getJobsMutableLiveData().observe(viewLifecycleOwner, Observer {
            jobsAdapter.homeJobs.submitList(it.toList())
        })

        return root
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position){
            0->{
                FiltroBusqueda = "PagoDescendente"
            }
            1->{
                FiltroBusqueda = "PagoAscendente"
            }
            2->{
                FiltroBusqueda = "FechaDescendente"
            }
            3->{
                FiltroBusqueda = "FechaAscendente"
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


    private fun setupRecyclerView(){
        jobsAdapter = JobAdapter() { IdTrabajo -> OnClickRecyclerViewCard_AdvanceSearch_getTrabajo(IdTrabajo) }
        recyclerView.apply{
            adapter = jobsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun OnClickRecyclerViewCard_AdvanceSearch_getTrabajo(IdTrabajo: BigInteger){
        this.listenerAdvanceSearch?.OnClickRecyclerViewCard_AdvanceSearch_getTrabajo(IdTrabajo)
    }

    fun BusquedaAvanzada() {
        AdvancedViewModel.resetData()

        val TituloTrabajoBuscar = et_TitleJob.text.toString()
        val PrecioDesdeTrabajoBuscar = if (et_paymentFrom.text.toString() != "") et_paymentFrom.text.toString() else null
        val PrecioHastaTrabajoBuscar = if (et_paymentTo.text.toString() != "") et_paymentTo.text.toString() else null
        val FechaDesdeTrabajoBuscar = if (et_dateFrom.text.toString() != "")et_dateFrom.text.toString()  else null
        val FechaHastaTrabajoBuscar = if (et_dateTo.text.toString() != "") et_dateTo.text.toString()  else null

        AdvancedViewModel.setTrabajoSearch(
            TituloTrabajoBuscar, PrecioDesdeTrabajoBuscar, PrecioHastaTrabajoBuscar,
            FechaDesdeTrabajoBuscar, FechaHastaTrabajoBuscar, FiltroBusqueda
        )

        AdvancedViewModel.getHomeJobs()
    }

    private fun showDatePickerDialog(textView: TextView) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog : DatePickerDialog = DatePickerDialog(requireContext(),
                {
                    datePicker, year, month, day ->
                    textView.text = "$year-${month + 1}-$day"


                    //TODO: AQUI RECIBIMOS EL DIA, MES Y A;O que el usuario eligio por lo tanto puedes implementar aqui lo que necesites para setear las fechas tambien
                    //TODO: Recuerda que esta funcion tiene el textview que mando llamar este metodo por si lo ocupas como validacion

                }, year, month, day)

        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnRecyclerViewActionsListener_AdvanceSearch){
            listenerAdvanceSearch = context
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !AdvancedViewModel.isLoading && !AdvancedViewModel.isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= HomeFragment.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                AdvancedViewModel.getHomeJobs()

                isScrolling = false
            }

        }
    }

}