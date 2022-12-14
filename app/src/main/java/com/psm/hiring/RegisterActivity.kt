package com.psm.hiring

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.psm.hiring.General.OnFragmentActionsListener_Registro
import com.psm.hiring.Modelos.RequestResponseAPI
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import com.psm.hiring.ui.register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern
import android.widget.Toast

import android.util.Patterns
import android.view.View

import android.widget.EditText
import com.psm.hiring.Modelos.Trabajos_Model
import java.util.*


class RegisterActivity : AppCompatActivity() , OnFragmentActionsListener_Registro {
    var UsuarioRegister: Usuarios_Model = Usuarios_Model()

    var Index_register : Int = 0
    lateinit var fragmentFocus:Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btn_next = findViewById<Button>(R.id.register_btn_next)
        val btn_back = findViewById<ImageButton>(R.id.register_imgBtn_back)

        val addFragment = Register1of3Fragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fl_Pasos_Registro,addFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        btn_next.setOnClickListener{
            var mFragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            getInfoFromFragment()

            when(Index_register){

                0->{

                    val editTxt_Email = findViewById<EditText>(R.id.register_editTxtv_email)
                    val editTxt_Password = findViewById<EditText>(R.id.register_editTxtv_password)
                    val validate_pass = isValidPassword(editTxt_Password.text.toString())
                    val image = findViewById<Button>(R.id.fg_register_btn_img)

                    val UsuarioCorreo = Usuarios_Model()
                    UsuarioCorreo.CorreoUsuario = editTxt_Email.text.toString()
                    val service: Service = RestEngine.getRestEngine().create(Service::class.java)
                    val result: Call<Usuarios_Model> = service.checarCorreo(UsuarioCorreo!!)
                    var message: String = ""

                    result.enqueue(object : Callback<Usuarios_Model> {

                        override fun onFailure(call: Call<Usuarios_Model>, t: Throwable) {
                            message = "error"
                        }

                        override fun onResponse(
                            call: Call<Usuarios_Model>,
                            response: Response<Usuarios_Model>
                        ) {
                            var respuesta: Usuarios_Model? = response.body()

                            if (respuesta?.IdUsuario != null) {
                                editTxt_Email.setError("Correo actual en uso, escriba otro")
                            }else{
                                if(editTxt_Email.text.isNullOrEmpty() || editTxt_Password.text.isNullOrEmpty() || UsuarioRegister.ImagenPerfilUsuario.isNullOrEmpty() ){

                                    if(editTxt_Email.text.isNullOrEmpty()){
                                        editTxt_Email.setError("Campo vacio")
                                    }
                                    if(editTxt_Password.text.isNullOrEmpty()){
                                        editTxt_Password.setError("Campo vacio")
                                    }

                                    if(UsuarioRegister.ImagenPerfilUsuario.isNullOrEmpty()){
                                        image.setError("Se necesita una imagen de usuario")
                                    }

                                }else{
                                    if(!Patterns.EMAIL_ADDRESS.matcher(editTxt_Email.text.toString()).matches()){
                                        editTxt_Email.setError("Formato de Correo incorrecto")
                                    }else{
                                        if(validate_pass != false){
                                            Index_register = 1

                                            fragmentFocus = Register2of3Fragment.setInfo(UsuarioRegister)
                                            supportFragmentManager.beginTransaction()
                                                .replace(R.id.fl_Pasos_Registro, fragmentFocus,"Register2of3Fragment")
                                                .commit()
                                        }else{
                                            editTxt_Password.setError("La contraseña debe tener un número, minuscula, mayuscula, caracter especial y un tamaño de 8 caracteres minimo")
                                        }
                                    }

                                }
                            }
                        }
                    })

                }
                1->{

                    val editTxt_Name = findViewById<EditText>(R.id.register_editTxtv_nombre)
                    val editTxt_app = findViewById<EditText>(R.id.register_editTxtv_app)
                    val editTxt_apm = findViewById<EditText>(R.id.register_editTxtv_apm)

                    if(editTxt_Name.text.isNullOrEmpty() || editTxt_app.text.isNullOrEmpty() || editTxt_apm.text.isNullOrEmpty()){
                        if(editTxt_Name.text.isNullOrEmpty()){
                            editTxt_Name.setError("Campo vacio")
                        }
                        if(editTxt_app.text.isNullOrEmpty()){
                            editTxt_app.setError("Campo vacio")
                        }
                        if(editTxt_apm.text.isNullOrEmpty()){
                            editTxt_apm.setError("Campo vacio")
                        }
                    }else{

                        Index_register = 2

                        fragmentFocus = Register3of3Fragment.setInfo(UsuarioRegister)
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_Pasos_Registro, fragmentFocus,"Register3of3Fragment")
                            .commit()

                    }

                }
                2->{

                    val editTxt_fn = findViewById<EditText>(R.id.register_editTxtv_fn)
                    val editTxt_pr = findViewById<EditText>(R.id.register_editTxtv_pr)
                    val editTxt_desc = findViewById<EditText>(R.id.register_editTxtv_desc)
                    editTxt_fn.setError(null)

                    if(editTxt_fn.text.isNullOrEmpty() || editTxt_pr.text.isNullOrEmpty() || editTxt_desc.text.isNullOrEmpty()){
                        if(editTxt_fn.text.isNullOrEmpty()){
                            editTxt_fn.setError("Campo vacio")
                        }
                        if(editTxt_pr.text.isNullOrEmpty()){
                            editTxt_pr.setError("Campo vacio")
                        }
                        if(editTxt_desc.text.isNullOrEmpty()){
                            editTxt_desc.setError("Campo vacio")
                        }
                    }else{
                        var new_bd = editTxt_fn.text.toString().replace("-", "/");
                        val nuevafecha = Date(new_bd)
                        if(Date().compareTo(nuevafecha)!! <= 0){
                            editTxt_fn.setError("Fecha de nacimiento no valida")
                            Toast.makeText(this, "Fecha de nacimiento no valida", Toast.LENGTH_SHORT).show()
                        }else {
                            createUser()
                        }
                    }

                }
            }


        }

        btn_back.setOnClickListener{
            getInfoFromFragment()

            when(Index_register){
                0->{
                    val vIntent =  Intent(this, LoginActivity::class.java)
                    startActivity(vIntent)
                }
                1->{
                    Index_register = 0
                    fragmentFocus = Register1of3Fragment.setInfo(UsuarioRegister)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_Pasos_Registro,fragmentFocus,"Register1of3Fragment")
                        .commit()
                }
                2->{
                    Index_register = 1
                    fragmentFocus = Register2of3Fragment.setInfo(UsuarioRegister)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_Pasos_Registro,fragmentFocus,"Register2of3Fragment")
                        .commit()
                }
            }


        }

    }

    fun getInfoFromFragment(){
        when(Index_register){
            0->{
                this.UsuarioRegister.CorreoUsuario = findViewById<EditText>(R.id.register_editTxtv_email).text.toString()?:this.UsuarioRegister.CorreoUsuario
                this.UsuarioRegister.PasswordUsuario = findViewById<EditText>(R.id.register_editTxtv_password).text.toString()?:this.UsuarioRegister.PasswordUsuario
            }
            1->{
                this.UsuarioRegister.NombreUsuario = findViewById<EditText>(R.id.register_editTxtv_nombre).text.toString()?:this.UsuarioRegister.NombreUsuario
                this.UsuarioRegister.ApellidoPaternoUsuario = findViewById<EditText>(R.id.register_editTxtv_app).text.toString()?:this.UsuarioRegister.ApellidoPaternoUsuario
                this.UsuarioRegister.ApellidoMaternoUsuario = findViewById<EditText>(R.id.register_editTxtv_apm).text.toString()?:this.UsuarioRegister.ApellidoMaternoUsuario
            }
            2->{
                this.UsuarioRegister.FechaNacimientoUsuario = findViewById<EditText>(R.id.register_editTxtv_fn).text.toString()?:this.UsuarioRegister.FechaNacimientoUsuario
                this.UsuarioRegister.ProfesionUsuario = findViewById<EditText>(R.id.register_editTxtv_pr).text.toString()?:this.UsuarioRegister.ProfesionUsuario
                this.UsuarioRegister.DescripcionUsuario = findViewById<EditText>(R.id.register_editTxtv_desc).text.toString()?:this.UsuarioRegister.DescripcionUsuario
            }
        }
    }

    override fun OnChangeFragmentSpinner_Registro_setEscolaridad(escolaridad: String) {
        this.UsuarioRegister.EscolaridadUsuario = escolaridad
    }

    override fun OnChangeFragmentImage_Registro_setImagenPerfil(ImagenB64: String) {
        this.UsuarioRegister.ImagenPerfilUsuario = ImagenB64
    }

    fun createUser(){
        DataManager.progressDialog = ProgressDialog(this)
        DataManager.progressDialog!!.setMessage("Creando usuario")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<RequestResponseAPI> = service.crearUsuario(this.UsuarioRegister!!)
        var message:String = ""

        result.enqueue(object: Callback<RequestResponseAPI> {

            override fun onFailure(call: Call<RequestResponseAPI>, t: Throwable){
                Toast.makeText(this@RegisterActivity, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()
                if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
            }

            override fun onResponse(call: Call<RequestResponseAPI>, response: Response<RequestResponseAPI>){
                var respuesta: RequestResponseAPI? =  response.body()

                if(respuesta?.status != "SUCCESS") {
                    message = "error"
                    Toast.makeText(this@RegisterActivity, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()
                    if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                }else{
                    Toast.makeText(this@RegisterActivity, "Usuario creado con exito", Toast.LENGTH_SHORT).show()
                    if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    val vIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(vIntent)
                }

            }
        })

    }

    fun isValidPassword(password : String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

}