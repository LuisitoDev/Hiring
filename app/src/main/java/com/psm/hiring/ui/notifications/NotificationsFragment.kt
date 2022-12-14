package com.psm.hiring.ui.notifications

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.hiring.General.OnRecyclerViewActionsListener_Notifications
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.adapters.NotificationAdapter
import com.psm.hiring.databinding.FragmentNotificationsBinding
import java.math.BigInteger


class NotificationsFragment : Fragment(), OnRecyclerViewActionsListener_Notifications {

    companion object {
        const val QUERY_PAGE_SIZE = 5
    }

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var notificationsAdapter: NotificationAdapter
    private var _binding: FragmentNotificationsBinding? = null

    private var listenerNotifications: OnRecyclerViewActionsListener_Notifications? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DataManager.progressDialog = ProgressDialog(DataManager.context)
        DataManager.progressDialog!!.setMessage("Cargando...")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        this.recyclerView = binding.rvNotifications
        //this.recyclerView.addOnScrollListener(this@NotificationsFragment.scrollListener)

        setupRecyclerView()

        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        notificationsViewModel.getUserMutableLiveData().observe(viewLifecycleOwner, Observer {
            notificationsAdapter.applicationsNotify.submitList(it.toList())
            //TODO: IMPLEMENAR ERROR DETENER BUSQUEDA
            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
        })

        return root
    }

    private fun setupRecyclerView(){
        notificationsAdapter = NotificationAdapter() { IdTrabajo -> OnClickRecyclerViewCard_Notifications_getTrabajo(IdTrabajo) }
        recyclerView.apply{
            adapter = notificationsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnRecyclerViewActionsListener_Notifications){
            listenerNotifications = context
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun OnClickRecyclerViewCard_Notifications_getTrabajo(IdTrabajo: BigInteger) {
        this.listenerNotifications?.OnClickRecyclerViewCard_Notifications_getTrabajo(IdTrabajo)
    }

    /*
    //CODIGO SI LA VENTANA TUVIERA PAGINACION
    var isLastPage = false
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

            val isNotLoadingAndNotLastPage = !notificationsViewModel.isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= NotificationsFragment.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                notificationsViewModel.getHomeJobs()
                isScrolling = false
            }

        }
    }
    */


}