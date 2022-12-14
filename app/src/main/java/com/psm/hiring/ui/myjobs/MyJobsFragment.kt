package com.psm.hiring.ui.myjobs

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
import com.psm.hiring.General.OnRecyclerViewActionsListener_MyJobs
import com.psm.hiring.adapters.MyJobsAdapter
import com.psm.hiring.databinding.FragmentMyJobsBinding
import java.math.BigInteger


class MyJobsFragment : Fragment(), OnRecyclerViewActionsListener_MyJobs {

    companion object {
        fun newInstance() = MyJobsFragment()
        const val QUERY_PAGE_SIZE = 5
    }

    private lateinit var myJobsViewModel: MyJobsViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var myJobsAdapter: MyJobsAdapter
    private var _binding: FragmentMyJobsBinding? = null

    private var listenerMyJobs: OnRecyclerViewActionsListener_MyJobs? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        _binding = FragmentMyJobsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        this.recyclerView = binding.rvMyjobs
        this.recyclerView.addOnScrollListener(this@MyJobsFragment.scrollListener)

        myJobsViewModel = ViewModelProvider(this).get(MyJobsViewModel::class.java)

        setupRecyclerView()

        myJobsViewModel.getMyJobsMutableLiveData().observe(viewLifecycleOwner, Observer{
            myJobsAdapter.myJobs.submitList(it.toList())
        })


        return root
    }


    private fun setupRecyclerView(){
        myJobsAdapter = MyJobsAdapter() { IdTrabajo -> OnClickRecyclerViewCard_MyJobs_getTrabajo(IdTrabajo) }
        recyclerView.apply{
            adapter = myJobsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnRecyclerViewActionsListener_MyJobs){
            listenerMyJobs = context
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun OnClickRecyclerViewCard_MyJobs_getTrabajo(IdTrabajo: BigInteger) {
        this.listenerMyJobs?.OnClickRecyclerViewCard_MyJobs_getTrabajo(IdTrabajo)
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

            val isNotLoadingAndNotLastPage = !myJobsViewModel.isLoading && !myJobsViewModel.isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= MyJobsFragment.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                myJobsViewModel.getMyJobs()
                isScrolling = false
            }

        }


    }

}