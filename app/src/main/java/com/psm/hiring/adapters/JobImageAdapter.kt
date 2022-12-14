package com.psm.hiring.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderAdapter
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Modelos.Archivos_Model
import com.psm.hiring.R
import com.psm.hiring.databinding.ItemApplicationcardBinding
import com.psm.hiring.databinding.ItemImageBinding
import com.psm.hiring.models.Application_Model

class JobImageAdapter(private val jobImages : ArrayList<Archivos_Model>)
    : CardSliderAdapter<JobImageAdapter.JobImageViewHolder>() {

    var context: Context? = null

    inner class JobImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobImageViewHolder {
        context = parent.context
        return JobImageViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindVH(holder: JobImageViewHolder, position: Int) {

        val jobImageReciever: Archivos_Model = jobImages[position]

        val base64Image = jobImageReciever.ArchivoData

        val strImage:String = base64Image?.replace("data:image/png;base64,","")
            ?.replace("data:image/jpeg;base64,","")
            ?.replace("data:image/png;base64,","")?.replace("dataimage/jpegbase64","")!!

        var byteArray = android.util.Base64.decode(strImage, android.util.Base64.DEFAULT)
        var BitmapImage = ImageUtilities.getBitMapFromByteArray(byteArray)

        holder.binding.ivImageJob.setImageBitmap(BitmapImage)
    }

    override fun getItemCount() : Int {
        return jobImages.size
    }
/*
    fun addImageToList(jobImage: Bitmap){
        jobImages.add(jobImage)
        notifyItemInserted(jobImages.size - 1)
    }*/

}