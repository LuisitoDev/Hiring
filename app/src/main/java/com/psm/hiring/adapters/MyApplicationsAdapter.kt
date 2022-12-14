package com.psm.hiring.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.R
import com.psm.hiring.databinding.ItemMyapplicationcardBinding
import java.math.BigInteger


class MyApplicationsAdapter(var OnClickRecyclerViewCard_MyApplications_getTrabajo: (BigInteger) -> Unit)
    : RecyclerView.Adapter<MyApplicationsAdapter.ViewHolder>(){

    var context: Context? = null

    private val myApplicationsCallback = object : DiffUtil.ItemCallback<Trabajos_Model>(){
        override fun areItemsTheSame(oldItem: Trabajos_Model, newItem: Trabajos_Model): Boolean {
            return oldItem.IdTrabajo == newItem.IdTrabajo
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Trabajos_Model, newItem: Trabajos_Model): Boolean {
            return oldItem == newItem
        }
    }

    val myApplications = AsyncListDiffer(this, myApplicationsCallback)

    inner class ViewHolder(val binding: ItemMyapplicationcardBinding): RecyclerView.ViewHolder(binding.root){
    //inner class ViewHolder(val binding: ItemJobcardBinding): RecyclerView.ViewHolder(binding.root){

        init{
            binding.cvJobcard.setOnClickListener{
                ClickOnCard()
            }
        }

        fun ClickOnCard(){

            var IdTrabajo = myApplications.currentList[bindingAdapterPosition].IdTrabajo
            OnClickRecyclerViewCard_MyApplications_getTrabajo(IdTrabajo!!)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemMyapplicationcardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trabajo : Trabajos_Model = myApplications.currentList[position]

        var byteArray:ByteArray? = null

        val strImage:String =  trabajo.ListaArchivosModel?.get(0)?.ArchivoData!!.replace("data:image/png;base64,","").replace("data:image/jpeg;base64,","")
        byteArray = Base64.decode(strImage, Base64.DEFAULT)
        holder.binding.ivJob.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

        holder.binding.tvJob.text =  holder.binding.tvJob.context.getString(R.string.title_jobcard, trabajo.TituloTrabajo)
        holder.binding.tvStatus.text = holder.binding.tvJob.context.getString(R.string.tv_status, trabajo.SolicitudAplicadaModel?.StatusSolicitud)
    }

    override fun getItemCount(): Int {
        return myApplications.currentList.size
    }


}