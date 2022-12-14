package com.psm.hiring.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psm.hiring.Database.UserApplication
import com.psm.hiring.Modelos.Solicitudes_Model
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationsViewModel : ViewModel() {

    private var _solicitudesLiveData : MutableLiveData<ArrayList<Solicitudes_Model>> = MutableLiveData<ArrayList<Solicitudes_Model>>()
    private var _solicitudesArrayList : ArrayList<Solicitudes_Model> = ArrayList()

    init {
        getNotificationsJobsApplitaction()
    }

    fun getUserMutableLiveData() : MutableLiveData<ArrayList<Solicitudes_Model>>{
        return _solicitudesLiveData
    }

    fun getNotificationsJobsApplitaction(){
        var solicitudes = Solicitudes_Model()

        solicitudes.UsuarioSolicita = DataManager.IdUsuario


        if (DataManager.isOnline()) {
            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<List<Solicitudes_Model>> =
                service.getSolicitudesNotificaciones(solicitudes!!)

            result.enqueue(object : Callback<List<Solicitudes_Model>> {

                override fun onFailure(call: Call<List<Solicitudes_Model>>, t: Throwable) {
                    val error: String = "error"

                    if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                }

                override fun onResponse(
                    call: Call<List<Solicitudes_Model>>,
                    response: Response<List<Solicitudes_Model>>
                ) {
                    var ListaSolicitudes = response.body()


                    if (ListaSolicitudes != null) {
                        UserApplication.dbHelper.insertarListaSolicitudesInfo(ListaSolicitudes)
                    }

                    updateMutableLiveDataMyJobs(ListaSolicitudes as ArrayList<Solicitudes_Model>)
                }
            })
        }
        else{
            val ListaSolicitudes = UserApplication.dbHelper.daoSoliciutdes.getNotificacionesSolicitudes(solicitudes.UsuarioSolicita!!)

            updateMutableLiveDataMyJobs(ListaSolicitudes as ArrayList<Solicitudes_Model>)

        }
    }

    private fun updateMutableLiveDataMyJobs(ListaSolicitudes : ArrayList<Solicitudes_Model>){

        /*
        if(_solicitudesArrayList.size == 0){
            _solicitudesArrayList = ListaSolicitudes
        }
        else{
            _solicitudesArrayList.addAll(ListaSolicitudes)
        }
        */

        _solicitudesLiveData.value = _solicitudesArrayList
        //_jobsLastIndex+=ListaTrabajos.size


        _solicitudesArrayList = ListaSolicitudes
        _solicitudesLiveData.value = _solicitudesArrayList
    }
}