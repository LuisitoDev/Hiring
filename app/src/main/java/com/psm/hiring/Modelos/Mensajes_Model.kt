package com.psm.hiring.Modelos

import java.math.BigInteger

class Mensajes_Model(){
    var IdMensaje: BigInteger? = null;
    var UsuarioEnvia: BigInteger? = null;
    var UsuarioRecibe: BigInteger? = null;
    var DescripcionMensaje:String? = null;
    var FechaCreacionMensaje:String? = null; //TODO: String?
    var EstadoMensaje:Byte? = null;

    var UsuarioEnviaModel: Usuarios_Model? = Usuarios_Model();
    var UsuarioRecibeModel: Usuarios_Model? = Usuarios_Model();

    private constructor(IdMensaje: BigInteger?, UsuarioEnvia: BigInteger?, UsuarioRecibe: BigInteger?,
                DescripcionMensaje:String?, FechaCreacionMensaje:String?, EstadoMensaje:Byte?,
                UsuarioEnviaModel: Usuarios_Model?, UsuarioRecibeModel: Usuarios_Model?) :this(){
        this.IdMensaje = IdMensaje
        this.UsuarioEnvia = UsuarioEnvia
        this.UsuarioRecibe = UsuarioRecibe
        this.DescripcionMensaje = DescripcionMensaje
        this.FechaCreacionMensaje = FechaCreacionMensaje
        this.EstadoMensaje = EstadoMensaje
        this.UsuarioEnviaModel = UsuarioEnviaModel
        this.UsuarioRecibeModel = UsuarioRecibeModel
    }


    data class Builder(
        var IdMensaje: BigInteger? = null,
        var UsuarioEnvia: BigInteger? = null,
        var UsuarioRecibe: BigInteger? = null,
        var DescripcionMensaje:String? = null,
        var FechaCreacionMensaje:String? = null,
        var EstadoMensaje:Byte? = null,
        var UsuarioEnviaModel: Usuarios_Model? = Usuarios_Model(),
        var UsuarioRecibeModel: Usuarios_Model? = Usuarios_Model()
    ) {

        fun setIdMensaje(IdMensaje: BigInteger) = apply { this.IdMensaje = IdMensaje }
        fun setUsuarioEnvia(UsuarioEnvia: BigInteger) = apply { this.UsuarioEnvia = UsuarioEnvia }
        fun setUsuarioRecibe(UsuarioRecibe: BigInteger) = apply { this.UsuarioRecibe = UsuarioRecibe }
        fun setDescripcionMensaje(DescripcionMensaje: String) = apply { this.DescripcionMensaje = DescripcionMensaje }
        fun setFechaCreacionMensaje(FechaCreacionMensaje: String) = apply { this.FechaCreacionMensaje = FechaCreacionMensaje }
        fun setEstadoMensaje(EstadoMensaje: Byte) = apply { this.EstadoMensaje = EstadoMensaje }
        fun setUsuarioEnviaModel(UsuarioEnviaModel: Usuarios_Model) = apply { this.UsuarioEnviaModel = UsuarioEnviaModel }
        fun setUsuarioRecibeModel(UsuarioRecibeModel: Usuarios_Model) = apply { this.UsuarioRecibeModel = UsuarioRecibeModel }


        fun build() = Mensajes_Model(   IdMensaje, UsuarioEnvia, UsuarioRecibe,
                                        DescripcionMensaje, FechaCreacionMensaje, EstadoMensaje,
                                        UsuarioEnviaModel, UsuarioRecibeModel)
    }
}