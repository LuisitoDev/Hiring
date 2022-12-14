package com.psm.hiring.ui.register

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.psm.hiring.General.OnFragmentActionsListener_Registro
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.R
import java.util.*

class Register3of3Fragment : Fragment(), AdapterView.OnItemSelectedListener{

    private var listenerRegistro: OnFragmentActionsListener_Registro? = null
    private lateinit var spinner : Spinner
    private lateinit var calendar : Calendar
    private lateinit var et_birthdate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_3de3_registro, container, false)


        val escolaridad = resources.getStringArray(R.array.Escolaridad)


        this.spinner = root.findViewById<Spinner>(R.id.register_spn_esc)
        this.et_birthdate = root.findViewById<EditText>(R.id.register_editTxtv_fn)
        calendar = Calendar.getInstance()

        et_birthdate.setOnClickListener {
            showDatePickerDialog(et_birthdate)
        }

        if (this.spinner != null) {
            val adapter =
                context?.let { ArrayAdapter(it, R.layout.spinner_style, escolaridad) }
            this.spinner.adapter = adapter
        }

        spinner.onItemSelectedListener  = this

        val bundle = arguments
        if (bundle?.isEmpty == false) {

            var fechaNacUsuario: String? = bundle!!.getString(Register3of3Fragment.ARG_FECHA_NAC_USUARIO) ?: ""
            var escolaridadUsuario = bundle!!.getString(Register3of3Fragment.ARG_ESCOLARIDAD_USUARIO) ?: ""
            var profesionUsuario = bundle!!.getString(Register3of3Fragment.ARG_PROFESION_USUARIO) ?: ""
            var descripcionUsuario = bundle!!.getString(Register3of3Fragment.ARG_DESCRIPCION_USUARIO) ?: ""

            var fechaNacUsuario_editTV = root.findViewById<EditText>(R.id.register_editTxtv_fn)
            var profesionUsuario_editTV = root.findViewById<EditText>(R.id.register_editTxtv_pr)
            var descripcionUsuario_editTV = root.findViewById<EditText>(R.id.register_editTxtv_desc)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentActionsListener_Registro){
            listenerRegistro = context
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val escolaridad: String = parent?.getItemAtPosition(position).toString()

        listenerRegistro?.OnChangeFragmentSpinner_Registro_setEscolaridad(escolaridad)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    companion object {
        const val ARG_FECHA_NAC_USUARIO = "FechaNacimientoUsuario"
        const val ARG_ESCOLARIDAD_USUARIO = "EscolaridadUsuario"
        const val ARG_PROFESION_USUARIO = "ProfesionUsuario"
        const val ARG_DESCRIPCION_USUARIO = "DescripcionUsuario"

        fun setInfo(usuarioRegister: Usuarios_Model): Register3of3Fragment {
            val fragment = Register3of3Fragment()

            if(usuarioRegister.FechaNacimientoUsuario != ""){
                val bundle = Bundle().apply {
                    putString(ARG_FECHA_NAC_USUARIO, usuarioRegister.FechaNacimientoUsuario)
                    putString(ARG_ESCOLARIDAD_USUARIO, usuarioRegister.EscolaridadUsuario)
                    putString(ARG_PROFESION_USUARIO, usuarioRegister.ProfesionUsuario)
                    putString(ARG_DESCRIPCION_USUARIO, usuarioRegister.DescripcionUsuario)
                }

                fragment.arguments = bundle
            }
            return fragment
        }

    }
}