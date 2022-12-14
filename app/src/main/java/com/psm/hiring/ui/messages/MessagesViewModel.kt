package com.psm.hiring.ui.messages

//import android.telecom.Call
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psm.hiring.Modelos.Mensajes_Model
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class MessagesViewModel : ViewModel() {
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is messages Fragment"
//    }
//    val text: LiveData<String> = _text


    private var _MensajesLiveData : MutableLiveData<ArrayList<Mensajes_Model>> = MutableLiveData<ArrayList<Mensajes_Model>>()
    private lateinit var _MensajesArrayList : ArrayList<Mensajes_Model>

    init {
        populateList()
    }

    fun getUsersMutableLiveData() : MutableLiveData<ArrayList<Mensajes_Model>>{
        return _MensajesLiveData
    }

    private fun populateList(){

        val mensaje = Mensajes_Model()
        mensaje.UsuarioEnvia = DataManager.IdUsuario

        _MensajesArrayList = ArrayList()

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Mensajes_Model>> = service.getConversaciones(mensaje!!)

        result.enqueue(object: Callback<List<Mensajes_Model>> {

            override fun onFailure(call: Call<List<Mensajes_Model>>, t: Throwable) {
                val error: String = "error"

                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
            }
            override fun onResponse(call: Call<List<Mensajes_Model>>,response: Response<List<Mensajes_Model>>) {
                var mensajeRespuesta =  response.body()

                updateMutableLiveDataMessage(mensajeRespuesta as ArrayList<Mensajes_Model>)
            }
        })
    }


    private fun updateMutableLiveDataMessage(mensajeRespuesta : ArrayList<Mensajes_Model>){
        if (mensajeRespuesta != null) {
            _MensajesArrayList = mensajeRespuesta
            _MensajesLiveData.value = _MensajesArrayList
        }
    }
}