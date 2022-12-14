package com.psm.hiring

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.psm.hiring.DAOs.Usuarios_DAO
import com.psm.hiring.Database.UserApplication
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.Utils.RestEngine
import com.psm.hiring.Utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(){

    companion object {
        var instance: LoginActivity? = null
    }

    init{
        instance = this
    }

    var btn_Login : Button? = null;
    var txtV_Register : TextView? = null;
    var editTextEmail : EditText? = null;
    var editTextPassword : EditText? = null;

    var UsuarioLoggin : Usuarios_Model? = Usuarios_Model();


    override fun onCreate(savedInstanceState: Bundle?) {
        val userApp : UserApplication = UserApplication()
        userApp.initialize(this@LoginActivity)


        DataManager.IdUsuario = null

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        this.btn_Login = findViewById<Button>(R.id.login_btn_entrar)
        this.txtV_Register = findViewById<TextView>(R.id.login_txtV_register)
        this.editTextEmail = findViewById<EditText>(R.id.login_editTxt_email)
        this.editTextPassword = findViewById<EditText>(R.id.login_editTxt_password)


        this.btn_Login?.setOnClickListener {
            logginUsuario(this)

        }

        this.txtV_Register?.setOnClickListener {
            val vIntent = Intent(this, RegisterActivity::class.java)
            startActivity(vIntent)
        }

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultValue = ""
        val IdUsuarioActivo = sharedPref.getString("IdUsuarioActivo", defaultValue)


        if(IdUsuarioActivo != ""){
            DataManager.IdUsuario = BigInteger(IdUsuarioActivo)

            val vIntent =  Intent(this, MenuActivity::class.java)
            startActivity(vIntent)
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = LoginActivity.instance?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("IdUsuarioActivo", "")
            apply()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        moveTaskToBack(true)
        exitProcess(-1)
    }

    fun logginUsuario(loginActivity: LoginActivity) {
        DataManager.progressDialog = ProgressDialog(this)
        DataManager.progressDialog!!.setMessage("Ingresando")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()
        this.UsuarioLoggin?.CorreoUsuario = editTextEmail?.text.toString()
        this.UsuarioLoggin?.PasswordUsuario = editTextPassword?.text.toString()

        if(UsuarioLoggin?.CorreoUsuario!!.isNullOrEmpty() || UsuarioLoggin?.PasswordUsuario!!.isNullOrEmpty()){

            if(editTextEmail?.text.isNullOrEmpty()){
                editTextEmail?.setError("Inserte un correo valido")
            }
            if(editTextPassword?.text.isNullOrEmpty()){
                editTextPassword?.setError("Campo vacio")
            }
            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
        }else{

            val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<Usuarios_Model> = service.loginUsuario(this.UsuarioLoggin!!)
            var message:String = ""

            result.enqueue(object: Callback<Usuarios_Model> {

                override fun onFailure(call: Call<Usuarios_Model>, t: Throwable){
                    message = "error"
                    if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    Toast.makeText(this@LoginActivity, "Hubo un error en la aplicación", Toast.LENGTH_SHORT).show()

                }

                override fun onResponse(call: Call<Usuarios_Model>, response: Response<Usuarios_Model>){
                    var respuestaUsuario: Usuarios_Model? =  response.body()

                    if (respuestaUsuario?.IdUsuario != null){
                        DataManager.IdUsuario = respuestaUsuario.IdUsuario

                        UserApplication.dbHelper.insertarUsuarioInLogin(respuestaUsuario)

                        val sharedPref = loginActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
                        with (sharedPref.edit()) {
                            putString("IdUsuarioActivo", respuestaUsuario.IdUsuario.toString())
                            apply()
                            if(DataManager.progressDialog!!.isShowing) {
                                val vIntent =  Intent(loginActivity, MenuActivity::class.java)
                                startActivity(vIntent)
                                DataManager.progressDialog!!.dismiss()
                            }
                        }

                    }
                    else{
                        message = "error, usuario no encontrado"
                        Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
                    }

                }

            })

        }

    }
}