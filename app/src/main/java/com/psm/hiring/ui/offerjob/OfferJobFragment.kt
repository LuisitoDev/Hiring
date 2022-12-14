package com.psm.hiring.ui.offerjob

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.islamkhsh.CardSliderViewPager
import com.psm.hiring.Database.UserApplication
import com.psm.hiring.General.OnFragmentActionsListener_OfferJob
import com.psm.hiring.Modelos.Archivos_Model
import com.psm.hiring.Modelos.RequestResponseAPI
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.Enum_Trabajo
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import com.psm.hiring.adapters.OfferJobImageAdapter
import com.psm.hiring.databinding.FragmentOfferJobBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.math.BigInteger

class OfferJobFragment : Fragment() {

    private lateinit var offerJobViewModel: OfferJobViewModel
    private var _binding: FragmentOfferJobBinding? = null

    private var OfferJobModel = Trabajos_Model()

    private var listenerOfferHob: OnFragmentActionsListener_OfferJob? = null

    val REQUEST_CODE = 100

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var nombreTrabajo_et : TextView
    private lateinit var descripcionTrabajo_et : TextView
    private lateinit var paymentTrabajo_et : TextView
    private lateinit var butonUploadJob_btn : Button
    private lateinit var butonSaveDraft_btn : Button
    private lateinit var jobimageCarousel : CardSliderViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataManager.progressDialog = ProgressDialog(DataManager.context)
        DataManager.progressDialog!!.setMessage("Cargando...")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()

        offerJobViewModel =
            ViewModelProvider(this).get(OfferJobViewModel::class.java)

        _binding = FragmentOfferJobBinding.inflate(inflater, container, false)
        val root: View = binding.root

        jobimageCarousel = binding.carouselImages

        offerJobViewModel.getJobFilesLiveData().observe(
            viewLifecycleOwner, Observer {
                jobimageCarousel.adapter = OfferJobImageAdapter(it as ArrayList<Archivos_Model>) { position ->
                    offerJobViewModel.removeImageFromArchivosModel(position)
                }
                //TODO: IMPLEMENAR ERROR DETENER BUSQUEDA
                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
            }
        )

        nombreTrabajo_et = root.findViewById<TextView>(R.id.et_jobTitle)
        descripcionTrabajo_et = root.findViewById<TextView>(R.id.et_jobDescription)
        paymentTrabajo_et = root.findViewById<TextView>(R.id.et_payment)

        butonUploadJob_btn = root.findViewById<Button>(R.id.btn_uploadJob)
        butonSaveDraft_btn = root.findViewById<Button>(R.id.btn_saveDraft)

        //Este es el boton para agregar imagenes.
        val uploadImage_btn = binding.fgEditBtnBack
        uploadImage_btn.setOnClickListener {
            openGalleryForImage()
        }

        val bundle = arguments
        var IdTrabajo: String? = ""
        var StatusTrabajo: String? = ""

        if (bundle == null) {
            //TODO: Cambiar texto de boton upload a "Subir"
            //TODO: Cambiar texto de boton job a "Guardar Borrador"

            butonUploadJob_btn.setOnClickListener{
                crearTrabajo(Enum_Trabajo.Trabajo_EnProceso.status)
            }

            butonSaveDraft_btn.setOnClickListener{
                crearTrabajo(Enum_Trabajo.Trabajo_Borrador.status)
            }

            butonUploadJob_btn.text = getString(R.string.string_create)
            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()

            butonUploadJob_btn.visibility = View.VISIBLE
            butonSaveDraft_btn.visibility = View.VISIBLE
        }
        else{
            butonUploadJob_btn.setOnClickListener{
                editarTrabajo(Enum_Trabajo.Trabajo_EnProceso.status)
            }


            butonSaveDraft_btn.setOnClickListener{
                editarTrabajo(Enum_Trabajo.Trabajo_Borrador.status)
            }


            IdTrabajo = bundle!!.getString(OfferJobFragment.ARG_ID_TRABAJO_OFFER_JOB) ?: ""
            offerJobViewModel.getTrabajoElegido(IdTrabajo)


            offerJobViewModel.getJobLiveData().observe(
                viewLifecycleOwner, Observer {
                    nombreTrabajo_et.setText(it.TituloTrabajo)
                    descripcionTrabajo_et.setText(it.DescripcionTrabajo)
                    paymentTrabajo_et.setText(it.PagoTrabajo)


                    this.OfferJobModel.IdTrabajo = it.IdTrabajo
                    this.OfferJobModel.StatusTrabajo = it.StatusTrabajo

                    when (this.OfferJobModel.StatusTrabajo) {
                        Enum_Trabajo.Trabajo_Borrador.status -> {
                            butonUploadJob_btn.visibility = View.VISIBLE
                            butonSaveDraft_btn.visibility = View.VISIBLE
                            butonUploadJob_btn.text = getString(R.string.string_create)
                        }
                        Enum_Trabajo.Trabajo_EnProceso.status -> {
                            butonUploadJob_btn.visibility = View.VISIBLE
                            butonSaveDraft_btn.visibility = View.GONE
                            butonUploadJob_btn.text = getString(R.string.string_edit)
                        }
                        else -> {
                            print("El stado del trabajo no es reconocido")
                        }
                    }

                }

                //TODO: IMPLEMENAR ERROR DETENER BUSQUEDA?
            )



        }

        return root
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imageUri : Uri? = data?.data
            val inputStream : InputStream? = imageUri?.let {
                context?.contentResolver?.openInputStream(it)
            }
            val imageBitmap : Bitmap = BitmapFactory.decodeStream(inputStream)

            offerJobViewModel.addImageToArchivosModel(OfferJobModel.IdTrabajo, imageBitmap)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentActionsListener_OfferJob){
            listenerOfferHob = context
        }
    }

    fun crearTrabajo(statusJob:String){
        FillInfoOfferJobModel(statusJob)

        if(OfferJobModel.TituloTrabajo.isNullOrEmpty() || OfferJobModel.DescripcionTrabajo.isNullOrEmpty()
            || OfferJobModel.PagoTrabajo.isNullOrEmpty() || OfferJobModel.ListaArchivosModel.isNullOrEmpty()){

            if(OfferJobModel.TituloTrabajo.isNullOrEmpty()){
                nombreTrabajo_et.setError("Campo vacio")
            }
            if(OfferJobModel.DescripcionTrabajo.isNullOrEmpty()){
                descripcionTrabajo_et.setError("Campo vacio")
            }
            if(OfferJobModel.PagoTrabajo.isNullOrEmpty()){
                paymentTrabajo_et.setError("Campo vacio")
            }
            if(OfferJobModel.ListaArchivosModel.isNullOrEmpty()){
                Toast.makeText(context, "Es necesario una imagen como minimo", Toast.LENGTH_SHORT).show()
            }

        }else{
            DataManager.progressDialog = ProgressDialog(context)
            DataManager.progressDialog!!.setMessage("Creando trabajo")
            DataManager.progressDialog!!.setCancelable(false)
            DataManager.progressDialog!!.show()
            if (DataManager.isOnline()) {
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)
                val result: Call<RequestResponseAPI> = service.crearTrabajo(this.OfferJobModel!!)
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
                            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                        } else {
                            //De la response Conseguimos el Id del Trabajo que acamaos de crear y lo mandamos por parametro
                            OfferJobModel.IdTrabajo = respuesta.IdTrabajo

                            GoToVisualizeGob(OfferJobModel.IdTrabajo)
                            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                        }

                    }
                })
            }
            else {
                OfferJobModel!!.IdTrabajo = UserApplication.dbHelper.daoTrabajos.getLowerIdTrabajo();
                OfferJobModel!!.EstadoTrabajo = 1

                var lowerIdArchivos = UserApplication.dbHelper.daoArchivos.getLowerIdArchivos()
                OfferJobModel.ListaArchivosModel?.forEach { itArchivoModel ->
                    itArchivoModel.IdArchivo = lowerIdArchivos
                    itArchivoModel.TrabajoAsignado = OfferJobModel!!.IdTrabajo
                    lowerIdArchivos = lowerIdArchivos!! - BigInteger("1")
                }

                UserApplication.dbHelper.crearTrabajoOfferJob(this.OfferJobModel!!)

                GoToVisualizeGob(OfferJobModel.IdTrabajo)
                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
            }
        }

    }


    fun editarTrabajo(statusJob:String){
        FillInfoOfferJobModel(statusJob)

        if(OfferJobModel.TituloTrabajo.isNullOrEmpty() || OfferJobModel.DescripcionTrabajo.isNullOrEmpty()
            || OfferJobModel.PagoTrabajo.isNullOrEmpty() || OfferJobModel.ListaArchivosModel.isNullOrEmpty()){

            if(OfferJobModel.TituloTrabajo.isNullOrEmpty()){
                nombreTrabajo_et.setError("Campo vacio")
            }
            if(OfferJobModel.DescripcionTrabajo.isNullOrEmpty()){
                descripcionTrabajo_et.setError("Campo vacio")
            }
            if(OfferJobModel.PagoTrabajo.isNullOrEmpty()){
                paymentTrabajo_et.setError("Campo vacio")
            }
            if(OfferJobModel.ListaArchivosModel.isNullOrEmpty()){
                Toast.makeText(context, "Es necesario una imagen como minimo", Toast.LENGTH_SHORT).show()
            }

        }else{
            DataManager.progressDialog = ProgressDialog(context)
            DataManager.progressDialog!!.setMessage("Editando trabajo")
            DataManager.progressDialog!!.setCancelable(false)
            DataManager.progressDialog!!.show()
            val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<RequestResponseAPI> = service.editarTrabajo(this.OfferJobModel!!)
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
                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }
                    else{
                        //Mandamos el Id Del Trabajo que acabamos de editar
                        GoToVisualizeGob(OfferJobModel.IdTrabajo)
                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }

                }
            })
        }

    }

    private fun FillInfoOfferJobModel(statusJob:String){
        this.OfferJobModel.TituloTrabajo = nombreTrabajo_et.text.toString()
        this.OfferJobModel.DescripcionTrabajo = descripcionTrabajo_et.text.toString()
        this.OfferJobModel.PagoTrabajo = paymentTrabajo_et.text.toString()

        this.OfferJobModel.StatusTrabajo = statusJob

        this.OfferJobModel.UsuarioCreador = DataManager.IdUsuario

        this.OfferJobModel.ListaArchivosModel = offerJobViewModel.getArchivos_Model()


    }

    private fun GoToVisualizeGob(IdTrabajo: BigInteger?){
        listenerOfferHob?.OnCliclFragmentButton_SaveOfferJob(IdTrabajo!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        const val ARG_ID_TRABAJO_OFFER_JOB = "IdTrabajo_OfferJob"

        /*
        fun newInstance() = visualizeJobFragment()

        fun setInfo(usuarioRegister: Trabajos_Model): visualizeJobFragment {

            val fragment = visualizeJobFragment()

            val bundle = Bundle().apply {
                putString(ARG_ID_TRABAJO_OFFER_JOB, usuarioRegister.IdTrabajo.toString())
            }

            fragment.arguments = bundle

            return fragment
        }
        */

    }
}