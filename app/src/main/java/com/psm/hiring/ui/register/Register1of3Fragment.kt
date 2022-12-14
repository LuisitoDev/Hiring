package com.psm.hiring.ui.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.General.OnFragmentActionsListener_Registro
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.R
import java.io.ByteArrayOutputStream

class Register1of3Fragment : Fragment(){

    val REQUEST_CODE = 100

    lateinit var root : View

    private var listenerRegistro: OnFragmentActionsListener_Registro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_1de3_registro, container, false)



        val btn_image = root.findViewById<Button>(R.id.fg_register_btn_img)

        btn_image.setOnClickListener(){
            openGalleryForImage()
        }

        val bundle = arguments

        if (bundle?.isEmpty == false) {
            var CorreoUsuario: String? = bundle!!.getString(ARG_CORREO_USUARIO) ?: ""
            var PasswordUsuario = bundle!!.getString(ARG_PASSWORD_USUARIO) ?: ""
            var ImagenPerfilUsuario = bundle!!.getString(ARG_IMAGEN_USUARIO) ?: ""

            var correoUsuario_editTV = root.findViewById<EditText>(R.id.register_editTxtv_email)
            var passwordUsuario_editTV =
                root.findViewById<EditText>(R.id.register_editTxtv_password)
            var imagenUsuario_imageV = root.findViewById<ImageView>(R.id.fg_register_imagen)

            correoUsuario_editTV.setText(CorreoUsuario)
            passwordUsuario_editTV.setText(PasswordUsuario)

            var byteArray: ByteArray? = null

            val strImage: String = ImagenPerfilUsuario!!.replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,", "")
            if (strImage!="") {
                byteArray = Base64.decode(strImage, Base64.DEFAULT)
                imagenUsuario_imageV.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
            }
        }

        return this.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentActionsListener_Registro){
            listenerRegistro = context
        }
    }


    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val image = root.findViewById<ImageView>(R.id.fg_register_imagen)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            image.setImageURI(data?.data) // handle chosen image
            var bitmap = (image.drawable as BitmapDrawable).bitmap

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            var Image64 = Base64.encodeToString(b, Base64.DEFAULT)

            listenerRegistro?.OnChangeFragmentImage_Registro_setImagenPerfil(Image64)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val ARG_CORREO_USUARIO = "CorreoUsuario"
        const val ARG_PASSWORD_USUARIO = "PasswordUsuario"
        const val ARG_IMAGEN_USUARIO = "ImagenPerfilUsuario"

        fun setInfo(usuarioRegister: Usuarios_Model): Register1of3Fragment {
            val fragment = Register1of3Fragment()

            val bundle = Bundle().apply {
                putString(ARG_CORREO_USUARIO, usuarioRegister.CorreoUsuario)
                putString(ARG_PASSWORD_USUARIO, usuarioRegister.PasswordUsuario)
                putString(ARG_IMAGEN_USUARIO, usuarioRegister.ImagenPerfilUsuario)
            }

            fragment.arguments = bundle

            return fragment
        }

    }

}