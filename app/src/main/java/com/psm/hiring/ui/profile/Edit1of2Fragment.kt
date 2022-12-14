package com.psm.hiring.ui.profile

import android.app.Activity
import android.app.ProgressDialog
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
import com.psm.hiring.General.OnFragmentActionsListener_Edit
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import java.io.ByteArrayOutputStream

class Edit1of2Fragment : Fragment() {
    val REQUEST_CODE = 100

    lateinit var root: View

    private var listenerMenu: OnFragmentActionsListener_Edit? = null

    var UsuarioEdit: Usuarios_Model = Usuarios_Model()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_1of2_edit_profile, container, false)


        val btn_image = root.findViewById<Button>(R.id.fg_edit_btn_img)

        btn_image.setOnClickListener(){
            openGalleryForImage()
        }

        val btn_Next = root.findViewById<Button>(R.id.fg_edit_btn_next)

        btn_Next.setOnClickListener(){
            setInfoUser()
            goToEdit2()
        }


        val bundle = arguments
        if (bundle?.isEmpty == false) {

            var NombreUsuario: String? = bundle!!.getString(Edit1of2Fragment.ARG_NOMBRE_USUARIO_EDIT_1) ?: ""
            var ApellidoPaternoUsuario = bundle!!.getString(Edit1of2Fragment.ARG_AP_PATERNO_USUARIO_EDIT_1) ?: ""
            var ApellidoMaternoUsuario = bundle!!.getString(Edit1of2Fragment.ARG_AP_MATERNO_USUARIO_EDIT_1) ?: ""

            //var ImagenPerfilUsuario = bundle!!.getString(Edit1of2Fragment.ARG_IMAGEN_USUARIO_EDIT_1) ?: ""
            var ImagenPerfilUsuario = DataManager.ImageAux

            var nombreUsuario_editTV = root?.findViewById<EditText>(R.id.edit_editTxtv_nombre)
            var appUsuario_editTV = root?.findViewById<EditText>(R.id.edit_editTxtv_app)
            var apmUsuario_editTV = root?.findViewById<EditText>(R.id.edit_editTxtv_apm)
            var imagenUsuario_imageV = root?.findViewById<ImageView>(R.id.fg_edit_imagen)

            UsuarioEdit.ImagenPerfilUsuario = ImagenPerfilUsuario

            nombreUsuario_editTV?.setText(NombreUsuario)
            appUsuario_editTV?.setText(ApellidoPaternoUsuario)
            apmUsuario_editTV?.setText(ApellidoMaternoUsuario)


            var byteArray: ByteArray? = null

            val strImage: String = ImagenPerfilUsuario!!.replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,", "")
            if (strImage!="") {
                byteArray = Base64.decode(strImage, Base64.DEFAULT)
                imagenUsuario_imageV?.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
            }



            var fechaNacUsuario: String? = bundle!!.getString(Edit1of2Fragment.ARG_FECHA_NAC_USUARIO_EDIT_1) ?: ""
            var escolaridadUsuario = bundle!!.getString(Edit1of2Fragment.ARG_ESCOLARIDAD_USUARIO_EDIT_1) ?: ""
            var profesionUsuario = bundle!!.getString(Edit1of2Fragment.ARG_PROFESION_USUARIO_EDIT_1) ?: ""
            var descripcionUsuario = bundle!!.getString(Edit1of2Fragment.ARG_DESCRIPCION_USUARIO_EDIT_1) ?: ""


            UsuarioEdit.FechaNacimientoUsuario = fechaNacUsuario
            UsuarioEdit.EscolaridadUsuario = escolaridadUsuario
            UsuarioEdit.ProfesionUsuario = profesionUsuario
            UsuarioEdit.DescripcionUsuario = descripcionUsuario
        }

        return root
    }


    private fun setInfoUser() {
        UsuarioEdit.NombreUsuario = root?.findViewById<EditText>(R.id.edit_editTxtv_nombre).text.toString()
        UsuarioEdit.ApellidoPaternoUsuario = root?.findViewById<EditText>(R.id.edit_editTxtv_app).text.toString()
        UsuarioEdit.ApellidoMaternoUsuario = root?.findViewById<EditText>(R.id.edit_editTxtv_apm).text.toString()
    }

    private fun goToEdit2() {
        val nombreUsuario_editTV = root?.findViewById<EditText>(R.id.edit_editTxtv_nombre)
        val appUsuario_editTV = root?.findViewById<EditText>(R.id.edit_editTxtv_app)
        val apmUsuario_editTV = root?.findViewById<EditText>(R.id.edit_editTxtv_apm)
        if(UsuarioEdit.NombreUsuario.isNullOrEmpty() || UsuarioEdit.ApellidoPaternoUsuario.isNullOrEmpty() || UsuarioEdit.ApellidoMaternoUsuario .isNullOrEmpty()){
            if(UsuarioEdit.NombreUsuario.isNullOrEmpty()){
                nombreUsuario_editTV.setError("Campo vacio")
            }
            if(UsuarioEdit.ApellidoPaternoUsuario.isNullOrEmpty()){
                appUsuario_editTV.setError("Campo vacio")
            }
            if(UsuarioEdit.ApellidoMaternoUsuario .isNullOrEmpty()){
                apmUsuario_editTV.setError("Campo vacio")
            }
        }else{
            listenerMenu?.OnClickFragmentButton_Edit1_GoToEdit2(UsuarioEdit)
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

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val image = root.findViewById<ImageView>(R.id.fg_edit_imagen)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            image.setImageURI(data?.data) // handle chosen image
            var bitmap = (image.drawable as BitmapDrawable).bitmap

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            var Image64 = Base64.encodeToString(b, Base64.DEFAULT)

            UsuarioEdit.ImagenPerfilUsuario = Image64
            //listener?.OnClickFragmentImgButton(Image64)
        }

    }

    companion object {
        const val ARG_IMAGEN_USUARIO_EDIT_1 = "ImagenPerfilUsuario_Edit_1"
        const val ARG_NOMBRE_USUARIO_EDIT_1 = "NombreUsuario_Edit_1"
        const val ARG_AP_PATERNO_USUARIO_EDIT_1 = "ApellidoPaternoUsuario_Edit_1"
        const val ARG_AP_MATERNO_USUARIO_EDIT_1 = "ApellidoMaternoUsuario_Edit_1"

        const val ARG_FECHA_NAC_USUARIO_EDIT_1 = "FechaNacimientoUsuario_Edit_1"
        const val ARG_ESCOLARIDAD_USUARIO_EDIT_1 = "EscolaridadUsuario_Edit_1"
        const val ARG_PROFESION_USUARIO_EDIT_1 = "ProfesionUsuario_Edit_1"
        const val ARG_DESCRIPCION_USUARIO_EDIT_1 = "DescripcionUsuario_Edit_1"

        fun setInfo(usuarioRegister: Usuarios_Model): Edit1of2Fragment {

            val fragment = Edit1of2Fragment()

            val bundle = Bundle().apply {
                putString(ARG_IMAGEN_USUARIO_EDIT_1, usuarioRegister.ImagenPerfilUsuario)
                putString(ARG_NOMBRE_USUARIO_EDIT_1, usuarioRegister.NombreUsuario)
                putString(ARG_AP_PATERNO_USUARIO_EDIT_1, usuarioRegister.ApellidoPaternoUsuario)
                putString(ARG_AP_MATERNO_USUARIO_EDIT_1, usuarioRegister.ApellidoMaternoUsuario)
            }

            fragment.arguments = bundle

            return fragment
        }

    }
}