package com.psm.hiring.Utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.psm.hiring.Database.UserApplication
import com.psm.hiring.Modelos.RequestResponseAPI
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.Modelos.Usuarios_Model
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

object DataManager {
    var IdUsuario:BigInteger? = null
    var context: Context? = null

    var progressDialog:  ProgressDialog? = null

    var ImageAux: String? = null

    fun isStringEmpty(stringVar:String?): Boolean {
        if(stringVar != null){
            if (stringVar != "")
                return false
            else
                return true
        }
        else
            return true
    }

    fun getUsuarioActivo(success: (usuarioResponse:Usuarios_Model?) -> Unit):Usuarios_Model{

        var ArgumentosUsuario = Usuarios_Model()
        ArgumentosUsuario.IdUsuario = this.IdUsuario
        if (isOnline()) {
            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<Usuarios_Model> = service.getUsuario(ArgumentosUsuario!!.IdUsuario!!)

            result.enqueue(object : Callback<Usuarios_Model> {

                override fun onFailure(call: Call<Usuarios_Model>, t: Throwable) {
                    val error: String = "error"

                    if(progressDialog!!.isShowing) progressDialog!!.dismiss()
                }

                override fun onResponse(
                    call: Call<Usuarios_Model>,
                    response: Response<Usuarios_Model>
                ) {
                    var usuarioResponse = response.body()
                    success(usuarioResponse)

                }
            })
        }
        var UsuarioActivoModel = ArgumentosUsuario

        return UsuarioActivoModel;
    }


    fun getTrabajo(IdTrabajo:BigInteger?, success: (trabajoResponse:Trabajos_Model?) -> Unit){

        val trabajo = Trabajos_Model()

        trabajo.IdTrabajo = IdTrabajo

        if (isOnline()) {

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
                    var trabajoResponse =  response.body()
                    success(trabajoResponse)
                }
            })
        }
    }


    fun isOnline(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun subirTrabajoLocalANube() : Int{
        var trabajosSubidos = 0
        if (DataManager.isOnline()) {

            var listaTrabajosLocales = UserApplication.dbHelper.daoTrabajos.getListMisTrabajosLocales(DataManager.IdUsuario!!)

            trabajosSubidos = listaTrabajosLocales.size
            listaTrabajosLocales.forEach { iTrabajoLocal ->

                val service: Service = RestEngine.getRestEngine().create(Service::class.java)
                val result: Call<RequestResponseAPI> = service.crearTrabajo(iTrabajoLocal!!)
                var message: String = ""

                result.enqueue(object : Callback<RequestResponseAPI> {

                    override fun onFailure(call: Call<RequestResponseAPI>, t: Throwable) {
                        message = "error"

                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }

                    override fun onResponse(
                        call: Call<RequestResponseAPI>,
                        response: Response<RequestResponseAPI>
                    ) {
                        var respuesta: RequestResponseAPI? = response.body()

                        if (respuesta?.status != "SUCCESS") {
                            message = "error"
                        } else {
                            message = "success"
                            //De la response Conseguimos el Id del Trabajo que acamaos de crear y lo mandamos por parametro
                            var trabajoIngresado = Trabajos_Model()
                            trabajoIngresado.IdTrabajo = respuesta.IdTrabajo

                            DataManager.getTrabajo(trabajoIngresado.IdTrabajo, { trabajoResponse ->
                                if (trabajoResponse != null) {
                                    trabajoIngresado = trabajoResponse

                                    UserApplication.dbHelper.crearTrabajoOfferJob(trabajoIngresado!!)
                                }
                            })

                        }

                    }
                })

                UserApplication.dbHelper.daoArchivos.deleteArchivosTrabajo(iTrabajoLocal.IdTrabajo!!)
                UserApplication.dbHelper.daoTrabajos.deleteTrabajo(iTrabajoLocal.IdTrabajo!!)

            }


        }

        return trabajosSubidos
    }



}