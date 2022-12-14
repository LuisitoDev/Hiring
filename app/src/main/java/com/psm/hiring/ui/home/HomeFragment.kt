package com.psm.hiring.ui.home

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.hiring.adapters.JobAdapter
import com.psm.hiring.databinding.FragmentHomeBinding

import com.psm.hiring.General.OnRecyclerViewActionsListener_Home
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigInteger

class HomeFragment : Fragment(), OnRecyclerViewActionsListener_Home{

    companion object {
        const val QUERY_PAGE_SIZE = 5
    }

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var jobsAdapter: JobAdapter
    private var _binding: FragmentHomeBinding? = null

    private var listenerMenu: OnRecyclerViewActionsListener_Home? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val searchBar = root?.findViewById<EditText>(R.id.et_search)

        var job: Job? = null
        searchBar.addTextChangedListener{ editable ->
            job?.cancel()
            job = MainScope().launch{
                delay(800L)
                editable?.let{
                    homeViewModel.resetData()

                    if(editable.toString().isNotEmpty()){
                        val TituloTrabajoBuscar = editable.toString()

                        homeViewModel.setTrabajoSearch(
                            TituloTrabajoBuscar, null, null,
                            null, null, null)

                        homeViewModel.busquedaAvanzada = true
                    }
                    else{
                        homeViewModel.busquedaAvanzada = false
                    }

                    homeViewModel.getHomeJobs()
                }
            }
        }

        val btnAdvanceSearch = root?.findViewById<ImageButton>(R.id.btn_lookingForSearch)
        btnAdvanceSearch.setOnClickListener{
            if (DataManager.isOnline())
                OnClickFragmentImage_Home_BusquedaAvanzada()
            else
                Toast.makeText(context, "Opcion disponible con Internet", Toast.LENGTH_SHORT).show()

        }

        this.recyclerView = binding.rvJobs
        this.recyclerView.addOnScrollListener(this@HomeFragment.scrollListener)


        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        homeViewModel.getHomeJobs()
        setupRecyclerView()

        homeViewModel.getJobsMutableLiveData().observe(viewLifecycleOwner, Observer {
            jobsAdapter.homeJobs.submitList(it.toList())
        })

        return root
    }

    private fun setupRecyclerView(){
        jobsAdapter = JobAdapter() { IdTrabajo -> OnClickRecyclerViewCard_Home_getTrabajo(IdTrabajo) }
        recyclerView.apply{
            adapter = jobsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnRecyclerViewActionsListener_Home){
            listenerMenu = context
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun OnClickRecyclerViewCard_Home_getTrabajo(IdTrabajo:BigInteger){
        this.listenerMenu?.OnClickRecyclerViewCard_Home_getTrabajo(IdTrabajo)
    }

    override fun OnClickFragmentImage_Home_BusquedaAvanzada() {
        this.listenerMenu?.OnClickFragmentImage_Home_BusquedaAvanzada()
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

            val isNotLoadingAndNotLastPage = !homeViewModel.isLoading && !homeViewModel.isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                                isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                homeViewModel.getHomeJobs()

                isScrolling = false
            }

        }
    }



}