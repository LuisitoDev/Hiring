package com.psm.hiring.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderAdapter
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Modelos.Archivos_Model
import com.psm.hiring.databinding.ItemImageBinding
import com.psm.hiring.databinding.ItemOfferimageBinding

class OfferJobImageAdapter(private val jobImages : ArrayList<Archivos_Model>, val removeItem : (Int) -> Unit)
    : CardSliderAdapter<OfferJobImageAdapter.OfferJobImageAdapterViewHolder>() {

    var context: Context? = null

    inner class OfferJobImageAdapterViewHolder(val binding: ItemOfferimageBinding) :
        RecyclerView.ViewHolder(binding.root){
            init {
                binding.btnErase.setOnClickListener {
                    eraseImage()
                }
            }

        private fun eraseImage() {
//            Toast.makeText(context, bindingAdapterPosition.toString(), Toast.LENGTH_SHORT).show()
            removeItem(bindingAdapterPosition)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferJobImageAdapterViewHolder {

        context = parent.context

        return OfferJobImageAdapterViewHolder(
            ItemOfferimageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindVH(holder: OfferJobImageAdapterViewHolder, position: Int) {

        val jobImageReciever: Archivos_Model = jobImages[position]

        val base64Image = jobImageReciever.ArchivoData

        val strImage: String = base64Image?.replace("data:image/png;base64,", "")
            ?.replace("data:image/jpeg;base64,", "")
            ?.replace("data:image/png;base64,", "")?.replace("dataimage/jpegbase64", "")!!

        var byteArray = android.util.Base64.decode(strImage, android.util.Base64.DEFAULT)
        var BitmapImage = ImageUtilities.getBitMapFromByteArray(byteArray)

        holder.binding.ivImageJob.setImageBitmap(BitmapImage)
    }

    override fun getItemCount(): Int {
        return jobImages.size
    }
}