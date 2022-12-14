package com.psm.hiring.ui.home

import android.app.ProgressDialog
import android.app.ProgressDialog.STYLE_HORIZONTAL
import android.app.ProgressDialog.STYLE_SPINNER
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psm.hiring.Database.UserApplication
//import com.psm.hiring.Modelos.Album
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.DataManager.context
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import retrofit2.*

class HomeViewModel : ViewModel() {

      private var _jobsLiveData : MutableLiveData<ArrayList<Trabajos_Model>> = MutableLiveData<ArrayList<Trabajos_Model>>()
      private var _jobsArrayList : ArrayList<Trabajos_Model> = ArrayList()
      var _jobsLastIndex = 0

      var isLoading = false
      var isLastPage = false
      var busquedaAvanzada = false
      var trabajoSearch : Trabajos_Model = Trabajos_Model()

      init {
      }

      fun getJobsMutableLiveData() : MutableLiveData<ArrayList<Trabajos_Model>>{
            return _jobsLiveData
      }

      fun getHomeJobs(){
            DataManager.progressDialog = ProgressDialog(context)
            DataManager.progressDialog!!.setMessage("Cargando...")
            DataManager.progressDialog!!.setCancelable(false)
            DataManager.progressDialog!!.show()

            if (DataManager.isOnline()) {
                  trabajoSearch.NumeroTrabajoPaginacion = _jobsLastIndex

                  val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                  var result: Call<List<Trabajos_Model>>
                  if (busquedaAvanzada == false)
                        result = service.getTrabajosInicio(trabajoSearch.NumeroTrabajoPaginacion!!)
                  else
                        result = service.getBusquedaAvanzadaTrabajos(trabajoSearch)

                  isLoading = true

                  result.enqueue(object : Callback<List<Trabajos_Model>> {

                        override fun onFailure(call: Call<List<Trabajos_Model>>, t: Throwable) {
                              val error: String = "error"

                              if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                              //Toast.makeText(this, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()
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

                                    updateMutableLiveDataHomeJobs(ListaTrabajos as ArrayList<Trabajos_Model>)
                                    if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()

                              } else
                                    isLastPage = true
                              if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                        }
                  })
            }
            else {

                  val ListaTrabajos = UserApplication.dbHelper.daoTrabajos.getListTrabajosHome()

                  updateMutableLiveDataHomeJobs(ListaTrabajos as ArrayList<Trabajos_Model>)

                  isLastPage = true
                  if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
            }

      }

      private fun updateMutableLiveDataHomeJobs(ListaTrabajos : ArrayList<Trabajos_Model>){

            if(_jobsArrayList.size == 0){
                  _jobsArrayList = ListaTrabajos
            }
            else{
                  _jobsArrayList.addAll(ListaTrabajos)
            }

            _jobsLiveData.value = _jobsArrayList
            _jobsLastIndex+=ListaTrabajos.size

            isLoading = false
      }

      fun setTrabajoSearch(TituloTrabajo: String?, PagoTrabajoDesde: String?, PagoTrabajoHasta: String?,
                         FechaDesdeCreacionTrabajo: String?, FechaHastaCreacionTrabajo: String?, FiltroBusqueda: String?) {
            trabajoSearch.TituloTrabajo = TituloTrabajo
            trabajoSearch.PagoTrabajoDesde = PagoTrabajoDesde
            trabajoSearch.PagoTrabajoHasta = PagoTrabajoHasta
            trabajoSearch.FechaDesdeCreacionTrabajo = FechaDesdeCreacionTrabajo
            trabajoSearch.FechaHastaCreacionTrabajo = FechaHastaCreacionTrabajo
            trabajoSearch.FiltroBusqueda = FiltroBusqueda
            trabajoSearch.NumeroTrabajoPaginacion = _jobsLastIndex
      }

      fun resetData(){
            _jobsArrayList = ArrayList()
            _jobsLiveData.value = _jobsArrayList
            _jobsLastIndex = 0

            isLoading = false
            isLastPage = false
      }


}