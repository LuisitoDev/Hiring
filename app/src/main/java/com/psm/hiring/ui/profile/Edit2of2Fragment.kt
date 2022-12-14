package com.psm.hiring.ui.profile

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.psm.hiring.General.OnFragmentActionsListener_Edit
import com.psm.hiring.Modelos.RequestResponseAPI
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import com.psm.hiring.ui.register.Register3of3Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Edit2of2Fragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var listenerMenu: OnFragmentActionsListener_Edit? = null

    private lateinit var spinner : Spinner;
    private lateinit var calendar : Calendar
    private lateinit var et_birthdate: EditText

    lateinit var root: View

    var UsuarioEdit: Usuarios_Model = Usuarios_Model()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_2of2_edit_profile, container, false)

        val escolaridad = resources.getStringArray(R.array.Escolaridad)

        this.et_birthdate = root.findViewById<EditText>(R.id.edit_editTxtv_fn)
        calendar = Calendar.getInstance()

        et_birthdate.setOnClickListener {
            showDatePickerDialog(et_birthdate)
        }

        this.spinner = root.findViewById<Spinner>(R.id.edit_spn_esc)
        if (this.spinner != null) {
            val adapter =
                context?.let { ArrayAdapter(it, R.layout.spinner_style, escolaridad) }
            this.spinner.adapter = adapter
        }

        spinner.onItemSelectedListener  = this


        val btn_Back = root.findViewById<Button>(R.id.fg_edit_btn_back)

        btn_Back.setOnClickListener(){
            setInfoUser()
            goToEdit1()
        }

        val btn_Save = root.findViewById<Button>(R.id.fg_edit_btn_save)

        btn_Save.setOnClickListener(){
            setInfoUser()
            saveInfoUser()
        }


        val bundle = arguments
        if (bundle?.isEmpty == false) {

            var fechaNacUsuario: String? = bundle!!.getString(Edit2of2Fragment.ARG_FECHA_NAC_USUARIO_EDIT_2) ?: ""
            var escolaridadUsuario = bundle!!.getString(Edit2of2Fragment.ARG_ESCOLARIDAD_USUARIO_EDIT_2) ?: ""
            var profesionUsuario = bundle!!.getString(Edit2of2Fragment.ARG_PROFESION_USUARIO_EDIT_2) ?: ""
            var descripcionUsuario = bundle!!.getString(Edit2of2Fragment.ARG_DESCRIPCION_USUARIO_EDIT_2) ?: ""

            var fechaNacUsuario_editTV = root.findViewById<EditText>(R.id.edit_editTxtv_fn)
            var profesionUsuario_editTV = root.findViewById<EditText>(R.id.edit_editTxtv_prof)
            var descripcionUsuario_editTV = root.findViewById<EditText>(R.id.edit_editTxtv_desc)

            var positionSpinner:Int = 0

            for (i in 0..escolaridad.size - 1) {
                if(escolaridad[i] == escolaridadUsuario){
                    positionSpinner = i
                    break
                }
            }

            fechaNacUsuario_editTV.setText(fechaNacUsuario)
            spinner.setSelection(positionSpinner)

            profesionUsuario_editTV.setText(profesionUsuario)
            descripcionUsuario_editTV.setText(descripcionUsuario)

            var NombreUsuario: String? = bundle!!.getString(Edit2of2Fragment.ARG_NOMBRE_USUARIO_EDIT_2) ?: ""
            var ApellidoPaternoUsuario = bundle!!.getString(Edit2of2Fragment.ARG_AP_PATERNO_USUARIO_EDIT_2) ?: ""
            var ApellidoMaternoUsuario = bundle!!.getString(Edit2of2Fragment.ARG_AP_MATERNO_USUARIO_EDIT_2) ?: ""
            var ImagenPerfilUsuario = DataManager.ImageAux
            //var ImagenPerfilUsuario = bundle!!.getString(Edit2of2Fragment.ARG_IMAGEN_USUARIO_EDIT_2) ?: ""


            UsuarioEdit.NombreUsuario = NombreUsuario
            UsuarioEdit.ApellidoPaternoUsuario = ApellidoPaternoUsuario
            UsuarioEdit.ApellidoMaternoUsuario = ApellidoMaternoUsuario
            UsuarioEdit.ImagenPerfilUsuario = ImagenPerfilUsuario
        }

        return root
    }

    private fun showDatePickerDialog(textView: TextView) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog : DatePickerDialog = DatePickerDialog(requireContext(),
            {
                    datePicker, year, month, day ->
                textView.text = "$year-${month + 1}-$day"

            }, year, month, day)

        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    private fun setInfoUser() {
        UsuarioEdit.FechaNacimientoUsuario = root?.findViewById<EditText>(R.id.edit_editTxtv_fn).text.toString()
        UsuarioEdit.ProfesionUsuario = root?.findViewById<EditText>(R.id.edit_editTxtv_prof).text.toString()
        UsuarioEdit.DescripcionUsuario = root?.findViewById<EditText>(R.id.edit_editTxtv_desc).text.toString()
    }

    private fun goToEdit1(){
        listenerMenu?.OnClickFragmentButton_Edit2_GoToEdit1(UsuarioEdit)
    }


    private fun saveInfoUser() {
        val fechaNacUsuario_editTV = root.findViewById<EditText>(R.id.edit_editTxtv_fn)
        val profesionUsuario_editTV = root.findViewById<EditText>(R.id.edit_editTxtv_prof)
        val descripcionUsuario_editTV = root.findViewById<EditText>(R.id.edit_editTxtv_desc)
        fechaNacUsuario_editTV.setError(null)

        if(UsuarioEdit.FechaNacimientoUsuario.isNullOrEmpty() || UsuarioEdit.ProfesionUsuario.isNullOrEmpty() || UsuarioEdit.DescripcionUsuario.isNullOrEmpty()){
            if(UsuarioEdit.FechaNacimientoUsuario.isNullOrEmpty()){
                fechaNacUsuario_editTV.setError("Campo vacio")
            }
            if(UsuarioEdit.ProfesionUsuario.isNullOrEmpty()){
                profesionUsuario_editTV.setError("Campo vacio")
            }
            if(UsuarioEdit.DescripcionUsuario.isNullOrEmpty()){
                descripcionUsuario_editTV.setError("Campo vacio")
            }
        }else{
            DataManager.progressDialog = ProgressDialog(context)
            DataManager.progressDialog!!.setMessage("Editando usuario")
            DataManager.progressDialog!!.setCancelable(false)
            DataManager.progressDialog!!.show()
            var new_bd = fechaNacUsuario_editTV.text.toString().replace("-", "/");
            val nuevafecha = Date(new_bd)
            if(Date().compareTo(nuevafecha)!! <= 0){
                fechaNacUsuario_editTV.setError("Fecha de nacimiento no valida")
                Toast.makeText(context, "Fecha de nacimiento no valida", Toast.LENGTH_SHORT).show()
                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
            }else{
                UsuarioEdit.IdUsuario = DataManager.IdUsuario

                val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
                val result: Call<RequestResponseAPI> = service.editarUsuario(this.UsuarioEdit!!)
                var message:String = ""

                result.enqueue(object: Callback<RequestResponseAPI> {

                    override fun onFailure(call: Call<RequestResponseAPI>, t: Throwable){
                        message = "error"
                        Toast.makeText(context, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()
                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }

                    override fun onResponse(call: Call<RequestResponseAPI>, response: Response<RequestResponseAPI>){
                        var respuesta: RequestResponseAPI? =  response.body()

                        if(respuesta?.status != "SUCCESS") {
                            message = "error"
                            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                        }
                        else{
                            Toast.makeText(context, "Cambios guardados con exito", Toast.LENGTH_SHORT).show()
                            listenerMenu?.OnClickFragmentButton_Edit2_SaveUser()
                            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                        }

                    }
                })
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentActionsListener_Edit){
            listenerMenu = context
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val escolaridad: String = parent?.getItemAtPosition(position).toString()

        UsuarioEdit.EscolaridadUsuario = escolaridad
        //listener?.OnClickFragmentButton(escolaridad)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    companion object {
        const val ARG_IMAGEN_USUARIO_EDIT_2 = "ImagenPerfilUsuario_Edit_2"
        const val ARG_NOMBRE_USUARIO_EDIT_2 = "NombreUsuario_Edit_2"
        const val ARG_AP_PATERNO_USUARIO_EDIT_2 = "ApellidoPaternoUsuario_Edit_2"
        const val ARG_AP_MATERNO_USUARIO_EDIT_2 = "ApellidoMaternoUsuario_Edit_2"

        const val ARG_FECHA_NAC_USUARIO_EDIT_2 = "FechaNacimientoUsuario_Edit_2"
        const val ARG_ESCOLARIDAD_USUARIO_EDIT_2 = "EscolaridadUsuario_Edit_2"
        const val ARG_PROFESION_USUARIO_EDIT_2 = "ProfesionUsuario_Edit_2"
        const val ARG_DESCRIPCION_USUARIO_EDIT_2 = "DescripcionUsuario_Edit_2"

        fun setInfo(usuarioRegister: Usuarios_Model): Register3of3Fragment {
            val fragment = Register3of3Fragment()

            if(usuarioRegister.FechaNacimientoUsuario != ""){
                val bundle = Bundle().apply {
                    putString(ARG_FECHA_NAC_USUARIO_EDIT_2, usuarioRegister.FechaNacimientoUsuario)
                    putString(ARG_ESCOLARIDAD_USUARIO_EDIT_2, usuarioRegister.EscolaridadUsuario)
                    putString(ARG_PROFESION_USUARIO_EDIT_2, usuarioRegister.ProfesionUsuario)
                    putString(ARG_DESCRIPCION_USUARIO_EDIT_2, usuarioRegister.DescripcionUsuario)
                }

                fragment.arguments = bundle
            }
            return fragment
        }

    }

}