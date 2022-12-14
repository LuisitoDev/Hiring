package com.psm.hiring.ui.myapplications

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psm.hiring.Database.UserApplication
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyApplicationsViewModel : ViewModel() {

    private var _myApplicationsLiveData : MutableLiveData<ArrayList<Trabajos_Model>> = MutableLiveData<ArrayList<Trabajos_Model>>()
    private var _myApplicationsArrayList : ArrayList<Trabajos_Model> = ArrayList()
    var _myApplicationLastIndex = 0

    var isLoading = false
    var isLastPage = false

    init {
        getMyApplications()
    }

    fun getMyApplicationsMutableLiveData() : MutableLiveData<ArrayList<Trabajos_Model>>{
        return _myApplicationsLiveData
    }

    fun getMyApplications(){

        DataManager.progressDialog = ProgressDialog(DataManager.context)
        DataManager.progressDialog!!.setMessage("Cargando...")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()

        val trabajo = Trabajos_Model()
        if (DataManager.isOnline()) {

            trabajo.UsuarioSolicita = DataManager.IdUsuario
            trabajo.StatusTrabajo = "En proceso"
            trabajo.NumeroTrabajoPaginacion = _myApplicationLastIndex

            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<List<Trabajos_Model>> = service.getMisSolicitudesTrabajo(trabajo!!)

            isLoading = true

            result.enqueue(object : Callback<List<Trabajos_Model>> {

                override fun onFailure(call: Call<List<Trabajos_Model>>, t: Throwable) {

                    if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    //Toast.makeText(this@MenuActivity, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<Trabajos_Model>>,
                    response: Response<List<Trabajos_Model>>
                ) {
                    var ListaTrabajos = response.body()
                    if (ListaTrabajos?.size != 0) {

                        if (ListaTrabajos != null) {
                            UserApplication.dbHelper.insertarListaTrabajosInfo(ListaTrabajos)
                            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                        }

                        updateMutableLiveDataApplications(ListaTrabajos as ArrayList<Trabajos_Model>)
                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }
                    else
                        isLastPage = true
                    if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                }
            })

        }
        else {
            val ListaTrabajos = UserApplication.dbHelper.daoTrabajos.getListMisTrabajosSolicitados(DataManager.IdUsuario!!)

            updateMutableLiveDataApplications(ListaTrabajos as ArrayList<Trabajos_Model>)

            isLastPage = true
            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
        }
    }

    private fun updateMutableLiveDataApplications(ListaTrabajos : ArrayList<Trabajos_Model>){

        if(_myApplicationsArrayList.size == 0){
            _myApplicationsArrayList = ListaTrabajos
        }
        else{
            _myApplicationsArrayList.addAll(ListaTrabajos)
        }

        _myApplicationsLiveData.value = _myApplicationsArrayList
        _myApplicationLastIndex+=ListaTrabajos.size


        isLoading = false
    }
}