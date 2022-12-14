package com.psm.hiring.ui.myapplications

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.hiring.General.OnRecyclerViewActionsListener_MyApplications
import com.psm.hiring.adapters.MyApplicationsAdapter
import com.psm.hiring.databinding.FragmentMyApplicationsBinding

import java.math.BigInteger

class MyApplicationsFragment : Fragment(), OnRecyclerViewActionsListener_MyApplications {

    companion object {
        fun newInstance() = MyApplicationsFragment()
        const val QUERY_PAGE_SIZE = 5
    }

    private lateinit var myApplicationsViewModel: MyApplicationsViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var myApplicationsAdapter: MyApplicationsAdapter

    private var _binding: FragmentMyApplicationsBinding? = null

    private var listenerMyapplications: OnRecyclerViewActionsListener_MyApplications? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        _binding = FragmentMyApplicationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        this.recyclerView = binding.rvMyapps
        this.recyclerView.addOnScrollListener(this@MyApplicationsFragment.scrollListener)

        myApplicationsViewModel = ViewModelProvider(this).get(MyApplicationsViewModel::class.java)

        setupRecyclerView()

        myApplicationsViewModel.getMyApplicationsMutableLiveData().observe(viewLifecycleOwner, Observer{
            myApplicationsAdapter.myApplications.submitList(it.toList())
        })

        return root
    }


    private fun setupRecyclerView(){
        myApplicationsAdapter = MyApplicationsAdapter() { IdTrabajo -> OnClickRecyclerViewCard_MyApplications_getTrabajo(IdTrabajo) }
        recyclerView.apply{
            adapter = myApplicationsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnRecyclerViewActionsListener_MyApplications){
            listenerMyapplications = context
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun OnClickRecyclerViewCard_MyApplications_getTrabajo(IdTrabajo: BigInteger) {
        this.listenerMyapplications?.OnClickRecyclerViewCard_MyApplications_getTrabajo(IdTrabajo)
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

            val isNotLoadingAndNotLastPage = !myApplicationsViewModel.isLoading && !myApplicationsViewModel.isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= MyApplicationsFragment.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                myApplicationsViewModel.getMyApplications()
                isScrolling = false
            }

        }
    }




}