package com.psm.hiring.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.R
import com.psm.hiring.databinding.ItemJobcardBinding

import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import java.math.BigInteger


class JobAdapter(var OnClickRecyclerViewCard_Home_getTrabajo: (BigInteger) -> Unit)
    : RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    var context: Context? = null

    private val homeJobsCallback = object : DiffUtil.ItemCallback<Trabajos_Model>(){
        override fun areItemsTheSame(oldItem: Trabajos_Model, newItem: Trabajos_Model): Boolean {
            return oldItem.IdTrabajo == newItem.IdTrabajo
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Trabajos_Model, newItem: Trabajos_Model): Boolean {
            return oldItem == newItem
        }
    }

    val homeJobs = AsyncListDiffer(this, homeJobsCallback)


    inner class ViewHolder(val binding: ItemJobcardBinding): RecyclerView.ViewHolder(binding.root) {

        init{
            binding.cvJobcard.setOnClickListener{
                ClickOnCard()
            }
        }

        fun ClickOnCard(){
            var IdTrabajo = homeJobs.currentList[bindingAdapterPosition].IdTrabajo
            OnClickRecyclerViewCard_Home_getTrabajo?.let { it(IdTrabajo!!) }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(ItemJobcardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /*
    override fun getItemViewType(position: Int): Int {
       return 1
    }
    */

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val trabajo = homeJobs.currentList[position]

        var byteArray:ByteArray? = null

        val strImage:String =  trabajo.ListaArchivosModel?.get(0)?.ArchivoData!!.replace("data:image/png;base64,","").replace("data:image/jpeg;base64,","")
        byteArray = Base64.decode(strImage, Base64.DEFAULT)
        holder.binding.ivJob.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))


        holder.binding.tvJob.text =  holder.binding.tvJob.context.getString(R.string.title_jobcard, trabajo.TituloTrabajo)
        holder.binding.tvContractor.text = holder.binding.tvJob.context.getString(R.string.subtitle_jobcard, trabajo.UsuarioCreadorModel?.NombreUsuario)

    }

    override fun getItemCount(): Int {
        return homeJobs.currentList.size
    }

}