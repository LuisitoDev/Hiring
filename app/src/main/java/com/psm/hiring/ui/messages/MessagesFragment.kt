package com.psm.hiring.ui.messages

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psm.hiring.Modelos.Mensajes_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.adapters.UserAdapter
import com.psm.hiring.databinding.FragmentMessagesBinding


class MessagesFragment : Fragment() {

    private lateinit var messagesViewModel: MessagesViewModel
    private var _binding: FragmentMessagesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataManager.progressDialog = ProgressDialog(DataManager.context)
        DataManager.progressDialog!!.setMessage("Cargando...")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()

        messagesViewModel = ViewModelProvider(this).get(MessagesViewModel::class.java)

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView : RecyclerView = binding.rvUsers
        messagesViewModel.getUsersMutableLiveData().observe(viewLifecycleOwner, Observer {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = UserAdapter(it as ArrayList<Mensajes_Model>)
            //TODO: IMPLEMENAR ERROR DETENER BUSQUEDA
            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}