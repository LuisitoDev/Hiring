package com.psm.hiring.ui.applications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psm.hiring.Modelos.Solicitudes_Model
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import com.psm.hiring.models.Application_Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class ApplicationsViewModel : ViewModel() {
    private var _applicationsLiveData : MutableLiveData<ArrayList<Solicitudes_Model>> = MutableLiveData<ArrayList<Solicitudes_Model>>()
    private lateinit var _applicationsArrayList : ArrayList<Solicitudes_Model>

    init {

    }

    fun getApplicationsMutableLiveData() : MutableLiveData<ArrayList<Solicitudes_Model>>{
        return _applicationsLiveData
    }

    fun getSolicitudesInfo(idTrabajo: String?){
        populatelist(idTrabajo)
    }

    private fun populatelist(idTrabajo : String?) {
        val solicitud = Solicitudes_Model()
        solicitud.TrabajoSolicitado = BigInteger(idTrabajo)
        solicitud.StatusSolicitud = "En proceso"

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Solicitudes_Model>> = service.getSolicitudesMiTrabajo(solicitud)

        result.enqueue(object: Callback<List<Solicitudes_Model>> {

            override fun onFailure(call: Call<List<Solicitudes_Model>>, t: Throwable){
                val error:String = "error"
                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
            }

            override fun onResponse(call: Call<List<Solicitudes_Model>>, response: Response<List<Solicitudes_Model>>){
                var solicitudesResponse =  response.body()
                updateMutableLiveDataVisualizeSolicitudes(solicitudesResponse as ArrayList<Solicitudes_Model>)
            }
        })
    }

    private fun updateMutableLiveDataVisualizeSolicitudes(solicitudesResponse : ArrayList<Solicitudes_Model>){
        _applicationsArrayList = solicitudesResponse
        _applicationsLiveData.value = _applicationsArrayList
    }

}
