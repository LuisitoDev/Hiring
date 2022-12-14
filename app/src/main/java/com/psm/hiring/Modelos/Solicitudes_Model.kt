package com.psm.hiring.Modelos

import java.math.BigInteger


class Solicitudes_Model(){
    var UsuarioSolicita: BigInteger? = null;
    var TrabajoSolicitado: BigInteger? = null;
    var StatusSolicitud:String? = null;
    var FechaCreacionSolicitud:String? = null; //TODO: String?
    var FechaRespuestaSolicitud:String? = null; //TODO: String?
    var EstadoSolicitud:Byte? = null;
    var UsuarioSolicitaModel:Usuarios_Model? = Usuarios_Model();
    var UsuarioCreadorModel:Usuarios_Model? = Usuarios_Model();


    private constructor(UsuarioSolicita: BigInteger?, TrabajoSolicitado: BigInteger?, StatusSolicitud:String?,
                FechaCreacionSolicitud:String?, FechaRespuestaSolicitud:String?, EstadoSolicitud:Byte?,
                        UsuarioSolicitaModel:Usuarios_Model?,UsuarioCreadorModel:Usuarios_Model?) :this(){
        this.UsuarioSolicita = UsuarioSolicita
        this.TrabajoSolicitado = TrabajoSolicitado
        this.StatusSolicitud = StatusSolicitud
        this.FechaCreacionSolicitud = FechaCreacionSolicitud
        this.FechaRespuestaSolicitud = FechaRespuestaSolicitud
        this.EstadoSolicitud = EstadoSolicitud
        this.UsuarioSolicitaModel = UsuarioSolicitaModel
        this.UsuarioCreadorModel = UsuarioCreadorModel
    }


    data class Builder(
        var UsuarioSolicita: BigInteger? = null,
        var TrabajoSolicitado: BigInteger? = null,
        var StatusSolicitud:String? = null,
        var FechaCreacionSolicitud:String? = null,
        var FechaRespuestaSolicitud:String? = null,
        var EstadoSolicitud:Byte? = null,
        var UsuarioSolicitaModel:Usuarios_Model? = null,
        var UsuarioCreadorModel:Usuarios_Model? = null) {


        fun setUsuarioSolicita(UsuarioSolicita: BigInteger) = apply { this.UsuarioSolicita = UsuarioSolicita }
        fun setTrabajoSolicitado(TrabajoSolicitado: BigInteger) = apply { this.TrabajoSolicitado = TrabajoSolicitado }
        fun setStatusSolicitud(StatusSolicitud: String) = apply { this.StatusSolicitud = StatusSolicitud }
        fun setFechaCreacionSolicitud(FechaCreacionSolicitud: String) = apply { this.FechaCreacionSolicitud = FechaCreacionSolicitud }
        fun setFechaRespuestaSolicitud(FechaCreacionMensaje: String) = apply { this.FechaRespuestaSolicitud = FechaRespuestaSolicitud }
        fun setEstadoSolicitud(EstadoSolicitud: Byte) = apply { this.EstadoSolicitud = EstadoSolicitud }
        fun setUsuarioSolicitaModel(UsuarioSolicitaModel: Usuarios_Model) = apply { this.UsuarioSolicitaModel = UsuarioSolicitaModel }
        fun setUsuarioCreadorModel(UsuarioCreadorModel: Usuarios_Model) = apply { this.UsuarioCreadorModel = UsuarioCreadorModel }


        fun build() = Solicitudes_Model(UsuarioSolicita, TrabajoSolicitado, StatusSolicitud,
                                        FechaCreacionSolicitud, FechaRespuestaSolicitud, EstadoSolicitud, UsuarioSolicitaModel,UsuarioCreadorModel)
    }
}
