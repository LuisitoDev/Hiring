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
import com.psm.hiring.databinding.ItemMyjobcardBinding
import java.math.BigInteger


class MyJobsAdapter(var OnClickRecyclerViewCard_MyJobs_getTrabajo: (BigInteger) -> Unit)
    : RecyclerView.Adapter<MyJobsAdapter.ViewHolder>() {

    var context: Context? = null

    private val myJobsCallback = object : DiffUtil.ItemCallback<Trabajos_Model>(){
        override fun areItemsTheSame(oldItem: Trabajos_Model, newItem: Trabajos_Model): Boolean {
            return oldItem.IdTrabajo == newItem.IdTrabajo
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Trabajos_Model, newItem: Trabajos_Model): Boolean {
            return oldItem == newItem
        }
    }

    val myJobs = AsyncListDiffer(this, myJobsCallback)



    inner class ViewHolder(val binding: ItemMyjobcardBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cvJobcard.setOnClickListener {
                ClickOnCard()
            }
        }

        private fun ClickOnCard() {            
            var IdTrabajo = myJobs.currentList[bindingAdapterPosition].IdTrabajo
            OnClickRecyclerViewCard_MyJobs_getTrabajo(IdTrabajo!!)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyJobsAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(ItemMyjobcardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyJobsAdapter.ViewHolder, position: Int) {
        val trabajo : Trabajos_Model = myJobs.currentList[position]

        var byteArray:ByteArray? = null

        val strImage:String =  trabajo.ListaArchivosModel?.get(0)?.ArchivoData!!.replace("data:image/png;base64,","").replace("data:image/jpeg;base64,","")
        byteArray = Base64.decode(strImage, Base64.DEFAULT)
        holder.binding.ivJob.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

        holder.binding.tvJob.text =  holder.binding.tvJob.context.getString(R.string.title_jobcard, trabajo.TituloTrabajo)
        holder.binding.tvStatus.text = holder.binding.tvJob.context.getString(R.string.tv_status, trabajo.StatusTrabajo)
    }

    override fun getItemCount(): Int {
        return myJobs.currentList.size
    }

}