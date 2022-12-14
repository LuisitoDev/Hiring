package com.psm.hiring.adapters

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Modelos.Solicitudes_Model
import com.psm.hiring.R
import com.psm.hiring.databinding.ItemHiredcardBinding
import com.psm.hiring.models.Application_Model

class HiredAdapter(val HiredArrayList: ArrayList<Solicitudes_Model>)
    : RecyclerView.Adapter<HiredAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHiredcardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiredAdapter.ViewHolder {
        return ViewHolder(
            ItemHiredcardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val application: Solicitudes_Model = HiredArrayList[position]

        holder.binding.ivUser.setImageResource(R.drawable.ic_launcher_foreground)
        holder.binding.tvUserName.text = application.UsuarioSolicitaModel?.getNombreCompleto()
        holder.binding.tvUserProfession.text = application.UsuarioSolicitaModel?.ProfesionUsuario

        var byteArray:ByteArray? = null

        val strImage:String? = application.UsuarioSolicitaModel?.ImagenPerfilUsuario?.replace("data:image/png;base64,","")
            ?.replace("data:image/jpeg;base64,","")
        byteArray = Base64.decode(strImage, Base64.DEFAULT )
        holder.binding.ivUser.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

        /*var strMessage: String = ""
        var byteArray: ByteArray? = null

        val strImage: String =
            trabajo.ListaArchivosModel?.get(0)?.ArchivoData!!.replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,", "")
        byteArray = Base64.decode(strImage, Base64.DEFAULT)
        holder.binding.ivJob.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))


        holder.binding.tvJob.text =
            holder.binding.tvJob.context.getString(R.string.title_jobcard, trabajo.TituloTrabajo)
        holder.binding.tvContractor.text = holder.binding.tvJob.context.getString(
            R.string.subtitle_jobcard,
            trabajo.UsuarioCreadorModel?.NombreUsuario
        )


        holder.binding.cvJobcard.setOnClickListener {
            //Toast.makeText(holder.itemView.context, trabajo.IdTrabajo.toString(),Toast.LENGTH_LONG).show()

            trabajo.IdTrabajo?.toInt()?.let { it1 -> Clickear(it1) }
        }*/

    }

    override fun getItemCount(): Int {
        return HiredArrayList.size
    }
}