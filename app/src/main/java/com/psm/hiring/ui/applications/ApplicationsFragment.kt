package com.psm.hiring.ui.applications

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.hiring.Modelos.RequestResponseAPI
import com.psm.hiring.Modelos.Solicitudes_Model
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import com.psm.hiring.adapters.ApplicationsAdapter
import com.psm.hiring.databinding.FragmentApplicationsBinding
import com.psm.hiring.models.Application_Model
import com.psm.hiring.ui.visualizejob.visualizeJobFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class ApplicationsFragment(var IdTrabajo : String) : Fragment() {

    private lateinit var applicationsViewModel: ApplicationsViewModel
    private var _binding: FragmentApplicationsBinding? = null

    private val binding get() = _binding!!

    //private var IdTrabajo : String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataManager.progressDialog = ProgressDialog(DataManager.context)
        DataManager.progressDialog!!.setMessage("Cargando...")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()

        _binding = FragmentApplicationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        applicationsViewModel = ViewModelProvider(this).get(ApplicationsViewModel::class.java)

        applicationsViewModel.getSolicitudesInfo(IdTrabajo)

        val recyclerView: RecyclerView = binding.rvApplications

        applicationsViewModel.getApplicationsMutableLiveData()
            .observe(viewLifecycleOwner, Observer {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter =
                    ApplicationsAdapter(it as ArrayList<Solicitudes_Model>) { IdUsuario,IdTrabajo,Respuesta ->
                        OnClickRecyclerViewCard_Solicitud_RefuseHire(IdUsuario,IdTrabajo,Respuesta)
                    }
                
                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
            })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        applicationsViewModel.getSolicitudesInfo(IdTrabajo)
    }

    fun OnClickRecyclerViewCard_Solicitud_RefuseHire(IdUsuario: BigInteger, IdTrabajo : BigInteger, Respuesta : String){
         val Solicitud = Solicitudes_Model()
        Solicitud.UsuarioSolicita = IdUsuario
        Solicitud.TrabajoSolicitado = IdTrabajo
        Solicitud.StatusSolicitud = Respuesta

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<RequestResponseAPI> = service.editarSolicitud(Solicitud!!)
        var message:String = ""

        result.enqueue(object: Callback<RequestResponseAPI> {

            override fun onFailure(call: Call<RequestResponseAPI>, t: Throwable){
                message = "error"
                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
            }

            override fun onResponse(call: Call<RequestResponseAPI>, response: Response<RequestResponseAPI>){
                var respuesta: RequestResponseAPI? =  response.body()

                if(respuesta?.status != "SUCCESS") {
                    message = "error"
                }

                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()

            }
        })

    }

    companion object {
        const val ARG_ID_SOLICITUD_VISUALIZE_JOB = "IdTrabajo_VisualizeJob"

    }

}