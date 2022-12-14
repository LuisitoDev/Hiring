package com.psm.hiring.ui.hired

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.hiring.Modelos.Solicitudes_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.adapters.HiredAdapter
import com.psm.hiring.databinding.FragmentHiredBinding
import com.psm.hiring.models.Application_Model

class HiredFragment(var IdTrabajo : String) : Fragment() {

    private lateinit var hiredViewModel: HiredViewModel
    private var _binding: FragmentHiredBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataManager.progressDialog = ProgressDialog(DataManager.context)
        DataManager.progressDialog!!.setMessage("Cargando...")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()

        _binding = FragmentHiredBinding.inflate(inflater, container, false)
        val root: View = binding.root

        hiredViewModel = ViewModelProvider(this).get(HiredViewModel::class.java)

        hiredViewModel.getSolicitudesInfo(IdTrabajo)

        val recyclerView : RecyclerView = binding.rvApplications

        hiredViewModel.getHiredMutableLiveData().observe(viewLifecycleOwner, Observer{
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = HiredAdapter(it as ArrayList<Solicitudes_Model>)
            //TODO: IMPLEMENAR ERROR DETENER BUSQUEDA
            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
        })

        return root
    }

    override fun onResume() {
        super.onResume()
        hiredViewModel.getSolicitudesInfo(IdTrabajo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}