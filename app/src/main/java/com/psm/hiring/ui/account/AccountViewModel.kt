package com.psm.hiring.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psm.hiring.Database.UserApplication
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.Utils.DataManager

class AccountViewModel : ViewModel() {

    var _usuarioLiveData : MutableLiveData<Usuarios_Model> = MutableLiveData<Usuarios_Model>()
    private var _usuarioModel : Usuarios_Model = Usuarios_Model()

    init {
        setUserModel()
    }

    fun getUserMutableLiveData() : MutableLiveData<Usuarios_Model>{
        return _usuarioLiveData
    }

    //Metodo que instancia los trabajos en un arraylist, ahorita esta en codigo duro, falta trabajarse con back, Considerar cambiar el nombre de la funcion

    private fun setUserModel(){

        if (DataManager.isOnline()) {
            DataManager.getUsuarioActivo({ usuarioResponse ->
                if (usuarioResponse != null) {
                    updateMutableLiveDataUsuario(usuarioResponse)
                }
            })
        }
        else{
            var IdUsuario = DataManager.IdUsuario
            if (IdUsuario != null) {
                val usuarioResponse = UserApplication.dbHelper.daoUsuarios.getUsuario(IdUsuario)

                if (usuarioResponse != null) {
                    updateMutableLiveDataUsuario(usuarioResponse)
                }
            }
        }
    }

    private fun updateMutableLiveDataUsuario(p_usuarioModel : Usuarios_Model){
        this._usuarioModel = p_usuarioModel
        this._usuarioLiveData.value = _usuarioModel
    }
}