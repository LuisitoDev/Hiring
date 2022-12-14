package com.psm.hiring.ui.offerjob

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psm.hiring.Modelos.Archivos_Model
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

import android.util.Base64
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Utils.DataManager
import java.io.ByteArrayOutputStream


class OfferJobViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is gallery Fragment"
//    }
//    val text: LiveData<String> = _text


    private val _jobLiveData : MutableLiveData<Trabajos_Model> = MutableLiveData<Trabajos_Model>()
    private var _trabajoModel = Trabajos_Model()

    private val _jobFilesLiveData : MutableLiveData<MutableList<Archivos_Model>> = MutableLiveData<MutableList<Archivos_Model>>()
    private var archivos_model = mutableListOf<Archivos_Model>()


    init {


        //setCarouselFiles(null)//TODO: QUITAR Y DEJARLO EN EL RESPONSE DE RETROFIT
    }

    fun getArchivos_Model(): MutableList<Archivos_Model> {
        return this.archivos_model
    }

    fun getJobFilesLiveData() : MutableLiveData<MutableList<Archivos_Model>>{
        return _jobFilesLiveData
    }

    fun getJobLiveData() : MutableLiveData<Trabajos_Model> {
        return _jobLiveData
    }

    fun getTrabajoElegido(p_IdtrabajoEditar : String?){
        populateModel(p_IdtrabajoEditar)
    }

    private fun populateModel(p_IdtrabajoEditar : String?){
        var ArgumentosTrabajo = Trabajos_Model()

        if(p_IdtrabajoEditar != null) {
            ArgumentosTrabajo.IdTrabajo = BigInteger(p_IdtrabajoEditar)

            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<Trabajos_Model> = service.getTrabajo(ArgumentosTrabajo!!.IdTrabajo!!)

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
                        updateMutableLiveDataTrabajo(trabajoResponse)
                    }
                }
            })
        }
        else{
            updateMutableLiveDataTrabajo(ArgumentosTrabajo) //ArgumentosTrabajoEmpty
        }
    }

    private fun updateMutableLiveDataTrabajo(p_trabajoModel : Trabajos_Model){
        this._trabajoModel = p_trabajoModel
        this._jobLiveData.value = _trabajoModel
        updateMutableLiveDataArchivos(this._trabajoModel.ListaArchivosModel)
    }

    private fun updateMutableLiveDataArchivos(p_archivoModel: MutableList<Archivos_Model>?){
        if (p_archivoModel != null) {
            this.archivos_model = p_archivoModel
        }
        this._jobFilesLiveData.value = archivos_model
    }

    fun addImageToArchivosModel(idTrabajo : BigInteger?, imageBitmap : Bitmap){
        val ArchivoModel = Archivos_Model()

        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        var Image64 = Base64.encodeToString(b, Base64.DEFAULT)

        ArchivoModel.ArchivoData = Image64

        if(idTrabajo != null) ArchivoModel.TrabajoAsignado = idTrabajo

        this.archivos_model.add(ArchivoModel)

        this._jobFilesLiveData.value = archivos_model
    }

    fun removeImageFromArchivosModel(imagePosition : Int){
        this.archivos_model.removeAt(imagePosition)
        this._jobFilesLiveData.value = archivos_model
    }
}