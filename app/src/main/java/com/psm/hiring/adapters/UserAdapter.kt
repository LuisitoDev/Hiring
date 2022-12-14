package com.psm.hiring.adapters

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Modelos.Mensajes_Model
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.databinding.ItemMessageCardBinding


class UserAdapter(val mensajesArrayList: ArrayList<Mensajes_Model>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMessageCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        return ViewHolder(ItemMessageCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val mensaje : Mensajes_Model = mensajesArrayList[position]

        //TODO: La forma de traer el string puede mejorar asignando el contexto a una variable local ahorita lo estamos trayendo con una view

        var byteArray:ByteArray? = null
        var strImage:String = ""
        if (mensaje.UsuarioEnvia == DataManager.IdUsuario){
            strImage =  mensaje.UsuarioRecibeModel?.ImagenPerfilUsuario!!.replace("data:image/png;base64,","").replace("data:image/jpeg;base64,","")
            byteArray = Base64.decode(strImage, Base64.DEFAULT)
            holder.binding.ivUser.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

            holder.binding.tvUserName.text = mensaje.UsuarioRecibeModel?.NombreUsuario
        }
        else{
            strImage =  mensaje.UsuarioEnviaModel?.ImagenPerfilUsuario!!.replace("data:image/png;base64,","").replace("data:image/jpeg;base64,","")
            byteArray = Base64.decode(strImage, Base64.DEFAULT)
            holder.binding.ivUser.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

            holder.binding.tvUserName.text = mensaje.UsuarioEnviaModel?.NombreUsuario
        }

    }

    override fun getItemCount(): Int {
        return mensajesArrayList.size
    }
}