package com.psm.hiring.ui.visualizejob

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psm.hiring.Database.UserApplication
import com.psm.hiring.Modelos.Archivos_Model
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class VisualizeJobViewModel: ViewModel() {

    private var _jobLiveData : MutableLiveData<Trabajos_Model> = MutableLiveData<Trabajos_Model>()
    private lateinit var _jobList : Trabajos_Model

    private val _jobFilesLiveData : MutableLiveData<MutableList<Archivos_Model>> = MutableLiveData<MutableList<Archivos_Model>>()
    private var archivos_model = mutableListOf<Archivos_Model>()

    init {

    }

    fun getJobMutableLiveData() : MutableLiveData<Trabajos_Model> {
//        _jobLiveData.value = _jobList
        return _jobLiveData
    }

    fun getJobFilesLiveData() : MutableLiveData<MutableList<Archivos_Model>>{
        return _jobFilesLiveData
    }

    fun getTrabajoInfo(idTrabajo: String?){
        populateList(idTrabajo)
    }

    private fun populateList(idTrabajo: String?) {
        val trabajo = Trabajos_Model()

        trabajo.IdTrabajo = BigInteger(idTrabajo)

        if (DataManager.isOnline()) {

            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<Trabajos_Model> = service.getTrabajo(trabajo.IdTrabajo!!)

            result.enqueue(object : Callback<Trabajos_Model> {

                override fun onFailure(call: Call<Trabajos_Model>, t: Throwable) {
                    val error: String = "error"

                    if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                }

                override fun onResponse(
                    call: Call<Trabajos_Model>,
                    response: Response<Trabajos_Model>
                ) {
                    var trabajoResponse = response.body()

                    if (trabajoResponse != null) {
                        var ListaTrabajos: MutableList<Trabajos_Model> = ArrayList<Trabajos_Model>()
                        ListaTrabajos.add(trabajoResponse)
                        UserApplication.dbHelper.insertarListaTrabajosInfo(ListaTrabajos)
                    }

                    updateMutableLiveDataVisualizeJob(trabajoResponse as Trabajos_Model)
                }
            })
        }
        else {
            val trabajoResponse = UserApplication.dbHelper.daoTrabajos.getDetallesTrabajo(trabajo.IdTrabajo!!)

            if (trabajoResponse != null) {
                updateMutableLiveDataVisualizeJob(trabajoResponse)
            }
        }
    }


    private fun updateMutableLiveDataVisualizeJob(trabajoResponse : Trabajos_Model){
        _jobList = trabajoResponse
        _jobLiveData.value = _jobList
        updateMutableLiveDataArchvios(this._jobList.ListaArchivosModel)
    }

    private fun updateMutableLiveDataArchvios(p_archivoModel : MutableList<Archivos_Model>?){
        if (p_archivoModel != null) {
            this.archivos_model = p_archivoModel
        }
        this._jobFilesLiveData.value = archivos_model
    }

}