package com.psm.hiring.ui.visualizejob

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.islamkhsh.CardSliderViewPager
import com.psm.hiring.General.OnFragmentActionsListener_Account
import com.psm.hiring.General.OnFragmentActionsListener_Job
import com.psm.hiring.Modelos.Archivos_Model
import com.psm.hiring.Modelos.RequestResponseAPI
import com.psm.hiring.Modelos.Solicitudes_Model
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import com.psm.hiring.adapters.JobImageAdapter
import com.psm.hiring.databinding.FragmentVisualizeJobBinding
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class visualizeJobFragment : Fragment() {

    private lateinit var jobViewModel: VisualizeJobViewModel
    private var _binding: FragmentVisualizeJobBinding? = null
    private lateinit var jobimageCarousel : CardSliderViewPager

    private var listenerMenu: OnFragmentActionsListener_Job? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var IdTrabajo : String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataManager.progressDialog = ProgressDialog(context)
        DataManager.progressDialog!!.setMessage("Cargando trabajo")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()
        _binding = FragmentVisualizeJobBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var descripcionTrabajo_TV = root?.findViewById<TextView>(R.id.descripcion_visualizeJob_tv)
        var nombreCreadorTrabajo_TV = root?.findViewById<TextView>(R.id.nombreUsuario_visualizeJob_tv)
        var puestoCreadorTrabajo_TV = root?.findViewById<TextView>(R.id.puestoUsuario_visualizeJob_tv)

        var pagoTrabajo_TV = root?.findViewById<TextView>(R.id.pagoTrabajo_visualizeJob_tv)
        var editarJob = root?.findViewById<Button>(R.id.btn_editJob)
        var btn_erase = root?.findViewById<Button>(R.id.btn_erase)
        var btn_close = root?.findViewById<Button>(R.id.btn_close)
        var verAplicaciones = root?.findViewById<Button>(R.id.btn_apps)
        var aplicar = root?.findViewById<Button>(R.id.btn_apply)
        var status = root?.findViewById<TextView>(R.id.tv_status_visualize)

        jobimageCarousel = binding.carouselImages

        val bundle = arguments
        //var IdTrabajo: String? = ""
        if (bundle?.isEmpty == false) {
            this.IdTrabajo = bundle!!.getString(visualizeJobFragment.ARG_ID_TRABAJO_VISUALIZE_JOB) ?: ""

        }
        else{
            var error = "hubo un error, no se mando un id"
            //TODO: DESPLEGAR UN ERROR Y SALIR DE LA VENTANA
        }


        jobViewModel = ViewModelProvider(this).get(VisualizeJobViewModel::class.java)

        jobViewModel.getTrabajoInfo(IdTrabajo)

        jobViewModel.getJobMutableLiveData().observe(viewLifecycleOwner,  Observer {

            //var tituloTrabajo_TV = root?.findViewById<EditText>(R.id.)

            //tituloTrabajo_TV?.text = it.TituloTrabajo
            if(it.EstadoTrabajo == 0.toByte()){
                aplicar.visibility = View.GONE
                editarJob.visibility = View.GONE
                verAplicaciones.visibility = View.GONE
                btn_erase.visibility = View.GONE
                btn_close.visibility = View.GONE
            }
            else {
                if (DataManager.IdUsuario == it.UsuarioCreador) {
                    aplicar.visibility = View.GONE

                    if (it.StatusTrabajo == "Cerrado") {
                        editarJob.visibility = View.GONE
                        btn_close.visibility = View.GONE
                    }

                } else {
                    editarJob.visibility = View.GONE
                    verAplicaciones.visibility = View.GONE
                    btn_erase.visibility = View.GONE
                    btn_close.visibility = View.GONE

                    if (it.StatusTrabajo == "Cerrado") {
                        aplicar.visibility = View.GONE
                    } else {
                        val Solicitud = Solicitudes_Model()
                        Solicitud.TrabajoSolicitado = BigInteger(IdTrabajo)
                        Solicitud.UsuarioSolicita = DataManager.IdUsuario

                        val service: Service =
                            RestEngine.getRestEngine().create(Service::class.java)
                        val result: Call<Solicitudes_Model> = service.getSolicitud(Solicitud!!)
                        var message: String = ""

                        result.enqueue(object : Callback<Solicitudes_Model> {

                            override fun onFailure(call: Call<Solicitudes_Model>, t: Throwable) {
                                message = "error"

                                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                            }

                            override fun onResponse(
                                call: Call<Solicitudes_Model>,
                                response: Response<Solicitudes_Model>
                            ) {
                                var respuesta: Solicitudes_Model? = response.body()

                                if (respuesta?.UsuarioSolicita == null) {
                                    message = "error"
                                } else {
                                    aplicar.visibility = View.GONE
                                    //Hacer visible status
                                    status.text = getString(R.string.tv_status, respuesta.StatusSolicitud)
                                    status.visibility = View.VISIBLE
                                }

                            }
                        })
                    }
                }
            }

            descripcionTrabajo_TV?.text = it.DescripcionTrabajo
            nombreCreadorTrabajo_TV?.text = it.UsuarioCreadorModel?.getNombreCompleto()
            pagoTrabajo_TV?.text = it.PagoTrabajo.toString()
            puestoCreadorTrabajo_TV?.text = it.UsuarioCreadorModel?.ProfesionUsuario

            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
        })

        jobViewModel.getJobFilesLiveData().observe(viewLifecycleOwner, Observer{
            jobimageCarousel.adapter = JobImageAdapter(it as ArrayList<Archivos_Model>)
        })

        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(VisualizeJobViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var editarJob = view.findViewById<Button>(R.id.btn_editJob)
        var verAplicaciones = view.findViewById<Button>(R.id.btn_apps)
        var applyJob = view.findViewById<Button>(R.id.btn_apply)
        val erase_job = view.findViewById<Button>(R.id.btn_erase)
        val close_job = view.findViewById<Button>(R.id.btn_close)
        var status = view.findViewById<TextView>(R.id.tv_status_visualize)

        editarJob.setOnClickListener {
            if (DataManager.isOnline())
                listenerMenu?.OnclickFragmentButton_Edit_EditJob(BigInteger(IdTrabajo))
            else
                Toast.makeText(context, "Opcion disponible con Internet", Toast.LENGTH_SHORT).show()
        }

        verAplicaciones.setOnClickListener {
            if (DataManager.isOnline())
                listenerMenu?.OnClickFragmentButton_SeeApplication_Job(BigInteger(IdTrabajo))
            else
                Toast.makeText(context, "Opcion disponible con Internet", Toast.LENGTH_SHORT).show()

        }

        erase_job.setOnClickListener {
            if (DataManager.isOnline()) {
                val Trabajo = Trabajos_Model()
                Trabajo.IdTrabajo = BigInteger(IdTrabajo)

                val service: Service = RestEngine.getRestEngine().create(Service::class.java)
                val result: Call<RequestResponseAPI> = service.deleteTrabajos(Trabajo!!)
                var message: String = ""

                result.enqueue(object : Callback<RequestResponseAPI> {

                    override fun onFailure(call: Call<RequestResponseAPI>, t: Throwable) {
                        message = "error"
                        Toast.makeText(context, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()

                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }

                    override fun onResponse(
                        call: Call<RequestResponseAPI>,
                        response: Response<RequestResponseAPI>
                    ) {
                        var respuesta: RequestResponseAPI? = response.body()

                        if (respuesta?.status != "SUCCESS") {
                            message = "error"
                            Toast.makeText(context, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()
                        } else {
                            listenerMenu?.OnClickFragmentButton_Erase_Jon()
                        }

                    }
                })
            }
            else{
                Toast.makeText(context, "Opcion disponible con Internet", Toast.LENGTH_SHORT).show()
            }
        }

        close_job.setOnClickListener {
            if (DataManager.isOnline()) {
                val Trabajo = Trabajos_Model()
                Trabajo.IdTrabajo = BigInteger(IdTrabajo)

                val service: Service = RestEngine.getRestEngine().create(Service::class.java)

                val result: Call<RequestResponseAPI> = service.closeTrabajos(Trabajo!!)
                var message: String = ""

                result.enqueue(object : Callback<RequestResponseAPI> {

                    override fun onFailure(call: Call<RequestResponseAPI>, t: Throwable) {
                        message = "error"
                        Toast.makeText(context, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()

                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }

                    override fun onResponse(
                        call: Call<RequestResponseAPI>,
                        response: Response<RequestResponseAPI>
                    ) {
                        var respuesta: RequestResponseAPI? = response.body()

                        if (respuesta?.status != "SUCCESS") {
                            message = "error"
                            Toast.makeText(context, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()
                        } else {
                            listenerMenu?.OnClickFragmentButton_Erase_Jon()
                        }

                    }
                })
            }
            else{
                Toast.makeText(context, "Opcion disponible con Internet", Toast.LENGTH_SHORT).show()
            }
        }

        applyJob.setOnClickListener {
            if (DataManager.isOnline()) {
                val Solicitud = Solicitudes_Model()
                Solicitud.TrabajoSolicitado = BigInteger(IdTrabajo)
                Solicitud.UsuarioSolicita = DataManager.IdUsuario

                val service: Service = RestEngine.getRestEngine().create(Service::class.java)
                val result: Call<RequestResponseAPI> = service.crearSolicitud(Solicitud!!)
                var message: String = ""

                result.enqueue(object : Callback<RequestResponseAPI> {

                    override fun onFailure(call: Call<RequestResponseAPI>, t: Throwable) {
                        message = "error"
                        Toast.makeText(context, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()

                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }

                    override fun onResponse(
                        call: Call<RequestResponseAPI>,
                        response: Response<RequestResponseAPI>
                    ) {
                        var respuesta: RequestResponseAPI? = response.body()

                        if (respuesta?.status != "SUCCESS") {
                            message = "error"
                            Toast.makeText(context, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()
                        } else {                            
                            applyJob.visibility = View.GONE
                            status.text = getString(R.string.tv_status, "En Proceso")
                            status.visibility = View.VISIBLE
                        }

                    }
                })

                //listenerMenu?.OnClickFragmentButton_Apply_Job(BigInteger(IdTrabajo))
            }
            else{
                Toast.makeText(context, "Opcion disponible con Internet", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentActionsListener_Job){
            listenerMenu = context
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        const val ARG_ID_TRABAJO_VISUALIZE_JOB = "IdTrabajo_VisualizeJob"

        fun newInstance() = visualizeJobFragment()

        fun setInfo(usuarioRegister: Trabajos_Model): visualizeJobFragment {

            val fragment = visualizeJobFragment()

            val bundle = Bundle().apply {
                putString(ARG_ID_TRABAJO_VISUALIZE_JOB, usuarioRegister.IdTrabajo.toString())
            }

            fragment.arguments = bundle

            return fragment
        }

    }
}