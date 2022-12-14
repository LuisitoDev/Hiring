package com.psm.hiring.Modelos

import java.math.BigInteger

class Archivos_Model(){
    var IdArchivo: BigInteger? = null;
    var ArchivoData:String? = null;
    var TrabajoAsignado: BigInteger? = null;

    private constructor(IdArchivo: BigInteger?, ArchivoData:String?, TrabajoAsignado: BigInteger?) :this(){
        this.IdArchivo = IdArchivo
        this.ArchivoData = ArchivoData
        this.TrabajoAsignado = TrabajoAsignado
    }

    data class Builder(
        var IdArchivo: BigInteger? = null,
        var ArchivoData: String? = null,
        var TrabajoAsignado: BigInteger? = null) {


        fun setIdArchivo(IdArchivo: BigInteger) = apply { this.IdArchivo = IdArchivo }
        fun setArchivoData(ArchivoData: String) = apply { this.ArchivoData = ArchivoData }
        fun setTrabajoAsignado(TrabajoAsignado: BigInteger) = apply { this.TrabajoAsignado = TrabajoAsignado }


        fun build() = Archivos_Model(IdArchivo, ArchivoData, TrabajoAsignado)
    }
}