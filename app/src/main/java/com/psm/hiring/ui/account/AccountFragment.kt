package com.psm.hiring.ui.account

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.General.OnFragmentActionsListener_Account
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.databinding.FragmentAccountBinding
import java.math.BigInteger

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null


    private var listenerMenu: OnFragmentActionsListener_Account? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataManager.progressDialog = ProgressDialog(DataManager.context)
        DataManager.progressDialog!!.setMessage("Cargando...")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()

        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var nombreUsuario_tv = root.findViewById<TextView>(R.id.tv_username)
        var edadUsuario_tv = root.findViewById<TextView>(R.id.tv_userAge)
        var escolaridadUsuario_tv = root.findViewById<TextView>(R.id.tv_userSchoolarship)
        var profesionUsuario_tv = root.findViewById<TextView>(R.id.tv_userProfession)
        var descripcionUsuario_tv = root.findViewById<TextView>(R.id.tv_curriculum)

        var imagenUsuario_tv = root.findViewById<ImageView>(R.id.iv_user)

        accountViewModel._usuarioLiveData.observe(viewLifecycleOwner,  Observer {
            nombreUsuario_tv.text = it.getNombreCompleto()
            edadUsuario_tv.text = getString(R.string.tv_age,it.getEdad())
            escolaridadUsuario_tv.text = it.EscolaridadUsuario
            profesionUsuario_tv.text = it.ProfesionUsuario
            descripcionUsuario_tv.text = it.DescripcionUsuario

            var byteArray:ByteArray? = null

            imagenUsuario_tv.setImageBitmap(ImageUtilities.getBitMapFromBase64(it.ImagenPerfilUsuario!!))

            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var editProfile_btn = view.findViewById<TextView>(R.id.btn_editProfile)
        var editPassword_btn = view.findViewById<Button>(R.id.btn_editPassword)

        editProfile_btn.setOnClickListener {
            if (DataManager.isOnline())
                listenerMenu?.OnClickFragmentButton_Account_EditarPerfil(accountViewModel._usuarioLiveData.value)
            else
                Toast.makeText(context, "Opcion disponible con Internet", Toast.LENGTH_SHORT).show()
        }

        editPassword_btn.setOnClickListener {
            if(DataManager.isOnline()){
                listenerMenu?.OnClickFragmentButton_Account_CambiarPassword()
            }else{
                Toast.makeText(context, "Opcion disponible con Internet", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentActionsListener_Account){
            listenerMenu = context
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}