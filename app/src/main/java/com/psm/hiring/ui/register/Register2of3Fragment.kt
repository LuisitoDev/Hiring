package com.psm.hiring.ui.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.psm.hiring.General.OnFragmentActionsListener_Registro
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.R

class Register2of3Fragment : Fragment() {

    private var listenerRegistro: OnFragmentActionsListener_Registro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_2de3_registro, container, false)

        val bundle = arguments
        if (bundle?.isEmpty == false) {

            var NombreUsuario: String? = bundle!!.getString(Register2of3Fragment.ARG_NOMBRE_USUARIO) ?: ""
            var ApellidoPaternoUsuario = bundle!!.getString(Register2of3Fragment.ARG_AP_PATERNO_USUARIO) ?: ""
            var ApellidoMaternoUsuario = bundle!!.getString(Register2of3Fragment.ARG_AP_MATERNO_USUARIO) ?: ""

            var nombreUsuario_editTV = root.findViewById<EditText>(R.id.register_editTxtv_nombre)
            var appUsuario_editTV = root.findViewById<EditText>(R.id.register_editTxtv_app)
            var apmUsuario_editTV = root.findViewById<EditText>(R.id.register_editTxtv_apm)

            nombreUsuario_editTV.setText(NombreUsuario)
            appUsuario_editTV.setText(ApellidoPaternoUsuario)
            apmUsuario_editTV.setText(ApellidoMaternoUsuario)
        }

        return root
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


    companion object {
        const val ARG_NOMBRE_USUARIO = "NombreUsuario"
        const val ARG_AP_PATERNO_USUARIO = "ApellidoPaternoUsuario"
        const val ARG_AP_MATERNO_USUARIO = "ApellidoMaternoUsuario"

        fun setInfo(usuarioRegister: Usuarios_Model): Register2of3Fragment {
            val fragment = Register2of3Fragment()

            val bundle = Bundle().apply {
                putString(ARG_NOMBRE_USUARIO, usuarioRegister.NombreUsuario)
                putString(ARG_AP_PATERNO_USUARIO, usuarioRegister.ApellidoPaternoUsuario)
                putString(ARG_AP_MATERNO_USUARIO, usuarioRegister.ApellidoMaternoUsuario)
            }

            fragment.arguments = bundle

            return fragment
        }

    }
}