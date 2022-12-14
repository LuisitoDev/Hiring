package com.psm.hiring.ui.changepassword

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.psm.hiring.General.OnFragmentActionsListener_Account
import com.psm.hiring.General.OnFragmentActionsListener_Registro
import com.psm.hiring.Modelos.RequestResponseAPI
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import com.psm.hiring.ui.register.Register2of3Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class ChangePasswordFragment : Fragment() {

    private var listenerEdit: OnFragmentActionsListener_Account? = null

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editarJob = view.findViewById<Button>(R.id.btn_saveDraft)

        editarJob.setOnClickListener {
            var new_password = view.findViewById<EditText>(R.id.et_change_password)
            val validate_pass = isValidPassword(new_password.text.toString())

            if(new_password.text.isNullOrEmpty()){

                new_password.setError("Campo vacio")

            }
            if(validate_pass != false){

                val UsuarioCorreo = Usuarios_Model()
                UsuarioCorreo.IdUsuario = DataManager.IdUsuario
                UsuarioCorreo.PasswordUsuario = new_password.text.toString()
                val service: Service = RestEngine.getRestEngine().create(Service::class.java)
                val result: Call<RequestResponseAPI> = service.editarPassword(UsuarioCorreo!!)
                var message: String = ""

                result.enqueue(object : Callback<RequestResponseAPI> {

                    override fun onFailure(call: Call<RequestResponseAPI>, t: Throwable) {
                        message = "error"
                        Toast.makeText(context, "Ocurrio un error en la base de datos", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<RequestResponseAPI>,
                        response: Response<RequestResponseAPI>
                    ) {
                        var respuesta: RequestResponseAPI? = response.body()

                        if (respuesta?.status != "SUCCESS") {
                            Toast.makeText(context, "Ocurrio un error al procesar la solicitud", Toast.LENGTH_SHORT).show()
                        }else{
                            listenerEdit?.OnClickFragmentButton_Account_EditarPassword()
                        }
                    }
                })
                
            }else{
                new_password.setError("La contraseña debe tener un número, minuscula, mayuscula, caracter especial y un tamaño de 8 caracteres minimo")
            }

        }

    }

    fun isValidPassword(password : String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentActionsListener_Account){
            listenerEdit = context
        }
    }

}