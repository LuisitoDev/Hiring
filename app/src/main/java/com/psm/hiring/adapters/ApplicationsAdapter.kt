package com.psm.hiring.adapters

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Modelos.Solicitudes_Model
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.R
import com.psm.hiring.databinding.ItemApplicationcardBinding
import com.psm.hiring.databinding.ItemJobcardBinding
import com.psm.hiring.models.Application_Model
import java.math.BigInteger
import android.view.View


//class ApplicationsAdapter(val ApplicationsArrayList: ArrayList<Application_Model>, var Clickear: (Int) -> Unit)
class ApplicationsAdapter(val ApplicationsArrayList: ArrayList<Solicitudes_Model> , var OnClickRecyclerViewCard_Solicitud_RefuseHire: (BigInteger,BigInteger,String) -> Unit)
    : RecyclerView.Adapter<ApplicationsAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemApplicationcardBinding) : RecyclerView.ViewHolder(binding.root){

        init{
            binding.btnRefuse.setOnClickListener {
                ClickOnRefuse()
            }

            binding.btnHire.setOnClickListener {
                ClickOnHire()
            }

        }

        fun ClickOnRefuse(){
            var IdUsuario =ApplicationsArrayList[bindingAdapterPosition].UsuarioSolicita
            var IdTrabajo =ApplicationsArrayList[bindingAdapterPosition].TrabajoSolicitado
            OnClickRecyclerViewCard_Solicitud_RefuseHire(IdUsuario!!,IdTrabajo!!,"Rechazada")

            //binding.root.visibility = View.GONE
            val params = binding.root.layoutParams
            params.height = 0
            params.width = 0
            binding.root.layoutParams = params
            /*
            binding.root.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(null)
            */

        }

        fun ClickOnHire(){
            var IdUsuario =ApplicationsArrayList[bindingAdapterPosition].UsuarioSolicita
            var IdTrabajo =ApplicationsArrayList[bindingAdapterPosition].TrabajoSolicitado
            OnClickRecyclerViewCard_Solicitud_RefuseHire(IdUsuario!!,IdTrabajo!!,"Aceptada")

            //binding.root.visibility = View.GONE
            val params = binding.root.layoutParams
            params.height = 0
            params.width = 0
            binding.root.layoutParams = params

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationsAdapter.ViewHolder {
        return ViewHolder(
            ItemApplicationcardBinding.inflate(
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
        val application: Solicitudes_Model = ApplicationsArrayList[position]

        holder.binding.ivUser.setImageResource(R.drawable.ic_launcher_foreground)
        holder.binding.tvUserName.text = application.UsuarioSolicitaModel?.getNombreCompleto()
        holder.binding.tvUserProfession.text = application.UsuarioSolicitaModel?.ProfesionUsuario

        /*
        holder.binding.btnRefuse.setOnClickListener {
            //holder.binding.root.layoutParams.height = 0
            //holder.binding.root.layoutParams.width = 0

            holder.binding.root.visibility = View.GONE
            val params = holder.binding.root.layoutParams
            params.height = 0
            params.width = 0
            holder.binding.root.layoutParams = params
        }
        */

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
        return ApplicationsArrayList.size
    }
}