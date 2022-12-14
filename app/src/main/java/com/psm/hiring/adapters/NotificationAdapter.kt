package com.psm.hiring.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Modelos.Solicitudes_Model
import com.psm.hiring.R
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.databinding.ItemNotificationsCardBinding
import java.math.BigInteger


class NotificationAdapter(var OnClickRecyclerViewCard_Notifications_getTrabajo: (BigInteger) -> Unit)
    : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    var context: Context? = null

    private val applicationsNotifyCallback = object : DiffUtil.ItemCallback<Solicitudes_Model>(){
        override fun areItemsTheSame(oldItem: Solicitudes_Model, newItem: Solicitudes_Model): Boolean {
            return oldItem.UsuarioSolicita == newItem.UsuarioSolicita && oldItem.TrabajoSolicitado == newItem.TrabajoSolicitado
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Solicitudes_Model, newItem: Solicitudes_Model): Boolean {
            return oldItem == newItem
        }
    }

    val applicationsNotify = AsyncListDiffer(this, applicationsNotifyCallback)

    inner class ViewHolder(val binding: ItemNotificationsCardBinding): RecyclerView.ViewHolder(binding.root){

        init{
            binding.cvNotifycard.setOnClickListener{
                ClickOnCard()
            }
        }

        fun ClickOnCard(){
            //Toast.makeText(context, differ.currentList[bindingAdapterPosition].TrabajoSolicitado.toString(),Toast.LENGTH_SHORT).show()

            var IdTrabajo = applicationsNotify.currentList[bindingAdapterPosition].TrabajoSolicitado
            OnClickRecyclerViewCard_Notifications_getTrabajo(IdTrabajo!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(ItemNotificationsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        val solicitud : Solicitudes_Model = applicationsNotify.currentList[position]

        var byteArray:ByteArray? = null

        val strImage:String =  solicitud.UsuarioCreadorModel?.ImagenPerfilUsuario!!.replace("data:image/png;base64,","").replace("data:image/jpeg;base64,","")
        byteArray = Base64.decode(strImage, Base64.DEFAULT)
        holder.binding.ivUser.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

        if(DataManager.IdUsuario == solicitud.UsuarioSolicita){
            //Usuario X hizo solicitud en tu trabajo
            holder.binding.tvNotification.text = holder.binding.tvNotification.context.getString(R.string.tv_notification, solicitud.UsuarioCreadorModel?.NombreUsuario)
            holder.binding.tvStatusNotify.text = holder.binding.tvStatusNotify.context.getString(R.string.tv_status, solicitud.StatusSolicitud)
        }
        else{
            holder.binding.tvNotification.text = holder.binding.tvNotification.context.getString(R.string.tv_notification_applied, solicitud.UsuarioCreadorModel?.NombreUsuario)
            holder.binding.tvStatusNotify.visibility = View.GONE
        }



    }

    override fun getItemCount(): Int {
        return applicationsNotify.currentList.size
    }
}