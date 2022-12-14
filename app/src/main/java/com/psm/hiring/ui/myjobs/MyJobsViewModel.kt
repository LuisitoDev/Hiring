package com.psm.hiring.ui.myjobs

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


class MyJobsViewModel : ViewModel() {
    private var _myJobsLiveData : MutableLiveData<ArrayList<Trabajos_Model>> = MutableLiveData<ArrayList<Trabajos_Model>>()
    private var _myJobsArrayList : ArrayList<Trabajos_Model> = ArrayList()
    var _myJobsLastIndex = 0

    var isLoading = false
    var isLastPage = false

    init {
        getMyJobs()
    }

    fun getMyJobsMutableLiveData() : MutableLiveData<ArrayList<Trabajos_Model>>{
        return _myJobsLiveData
    }

    fun getMyJobs(){
        DataManager.progressDialog = ProgressDialog(DataManager.context)
        DataManager.progressDialog!!.setMessage("Cargando...")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()

        val trabajo = Trabajos_Model()

        if (DataManager.isOnline()) {
            trabajo.UsuarioCreador = DataManager.IdUsuario
            trabajo.StatusTrabajo = null
            trabajo.NumeroTrabajoPaginacion = _myJobsLastIndex

            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<List<Trabajos_Model>> = service.getMisTrabajos(trabajo!!)

            isLoading = true

            result.enqueue(object : Callback<List<Trabajos_Model>> {

                override fun onFailure(call: Call<List<Trabajos_Model>>, t: Throwable) {
                    val error: String = "error"

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

                        updateMutableLiveDataMyJobs(ListaTrabajos as ArrayList<Trabajos_Model>)
                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }
                    else
                        isLastPage = true
                    if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                }
            })
        }
        else {

            val ListaTrabajos = UserApplication.dbHelper.daoTrabajos.getListMisTrabajos(DataManager.IdUsuario!!)

            updateMutableLiveDataMyJobs(ListaTrabajos as ArrayList<Trabajos_Model>)

            isLastPage = true
            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
        }
    }

    private fun updateMutableLiveDataMyJobs(ListaTrabajos : ArrayList<Trabajos_Model>){

        if(_myJobsArrayList.size == 0){
            _myJobsArrayList = ListaTrabajos
        }
        else{
            _myJobsArrayList.addAll(ListaTrabajos)
        }

        _myJobsLiveData.value = _myJobsArrayList
        _myJobsLastIndex+=ListaTrabajos.size

        isLoading = false
    }
}