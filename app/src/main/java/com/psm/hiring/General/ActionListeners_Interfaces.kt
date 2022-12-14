package com.psm.hiring.General

import com.psm.hiring.Modelos.Usuarios_Model
import java.math.BigInteger

interface OnRecyclerViewActionsListener {
    fun OnClickRecyclerView(IdTrabajo:Int)
}

interface OnRecyclerViewActionsListener_Home {
    fun OnClickRecyclerViewCard_Home_getTrabajo(IdTrabajo: BigInteger)

    fun OnClickFragmentImage_Home_BusquedaAvanzada()
}


interface OnRecyclerViewActionsListener_AdvanceSearch {
    fun OnClickRecyclerViewCard_AdvanceSearch_getTrabajo(IdTrabajo: BigInteger)
}



interface OnRecyclerViewActionsListener_MyJobs {
    fun OnClickRecyclerViewCard_MyJobs_getTrabajo(IdTrabajo: BigInteger)
}

interface OnRecyclerViewActionsListener_MyApplications {
    fun OnClickRecyclerViewCard_MyApplications_getTrabajo(IdTrabajo: BigInteger)
}

interface OnFragmentActionsListener_Job{
    fun OnclickFragmentButton_Edit_EditJob(IdTrabajo: BigInteger)

    fun OnClickFragmentButton_SeeApplication_Job(IdTrabajo: BigInteger)

    fun OnClickFragmentButton_Apply_Job(IdTrabajo: BigInteger)

    fun OnClickFragmentButton_Erase_Jon()

}

interface OnRecyclerViewActionsListener_Notifications {
    fun OnClickRecyclerViewCard_Notifications_getTrabajo(IdTrabajo: BigInteger)
}

interface OnFragmentActionsListener_Registro {
    fun OnChangeFragmentSpinner_Registro_setEscolaridad(escolaridad:String)

    fun OnChangeFragmentImage_Registro_setImagenPerfil(ImagenB64:String)
}

interface OnFragmentActionsListener_Account {
    fun OnClickFragmentButton_Account_EditarPerfil(p_Usuario: Usuarios_Model?)

    fun OnClickFragmentButton_Account_CambiarPassword()

    fun OnClickFragmentButton_Account_EditarPassword()

}

interface OnFragmentActionsListener_Edit {
    fun OnClickFragmentButton_Edit1_GoToEdit2(p_Usuario: Usuarios_Model)

    fun OnClickFragmentButton_Edit2_GoToEdit1(p_Usuario: Usuarios_Model)

    fun OnClickFragmentButton_Edit2_SaveUser()
}


interface OnFragmentActionsListener_OfferJob {
    fun OnCliclFragmentButton_SaveOfferJob(IdTrabajo: BigInteger)
}
