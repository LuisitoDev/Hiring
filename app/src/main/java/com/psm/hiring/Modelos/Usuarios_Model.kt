package com.psm.hiring.Modelos

import java.math.BigInteger
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*


class Usuarios_Model(){
    var IdUsuario: BigInteger? = null;
    var NombreUsuario:String? = null;
    var ApellidoPaternoUsuario:String? = null;
    var ApellidoMaternoUsuario: String? = null;
    var FechaNacimientoUsuario:String? = null; //TODO: String?
    var EscolaridadUsuario:String? = null;
    var ProfesionUsuario:String? = null;
    var DescripcionUsuario:String? = null;
    var ImagenPerfilUsuario:String? = null;
    var CorreoUsuario: String? = null;
    var PasswordUsuario:String? = null;
    var FechaCreacionUsuario:String? = null; //TODO: String?
    var EstadoUsuario:Byte? = null;

    private constructor(IdUsuario: BigInteger?, NombreUsuario:String?, ApellidoPaternoUsuario:String?, ApellidoMaternoUsuario: String?,
                FechaNacimientoUsuario:String?, EscolaridadUsuario:String?, ProfesionUsuario:String?, DescripcionUsuario:String?,
                ImagenPerfilUsuario:String?, CorreoUsuario: String?, PasswordUsuario:String?, FechaCreacionUsuario:String?, EstadoUsuario:Byte?) :this(){
        this.IdUsuario = IdUsuario
        this.NombreUsuario = NombreUsuario
        this.ApellidoPaternoUsuario =ApellidoPaternoUsuario
        this.ApellidoMaternoUsuario = ApellidoMaternoUsuario
        this.FechaNacimientoUsuario = FechaNacimientoUsuario
        this.EscolaridadUsuario = EscolaridadUsuario
        this.ProfesionUsuario = ProfesionUsuario
        this.DescripcionUsuario = DescripcionUsuario
        this.ImagenPerfilUsuario = ImagenPerfilUsuario
        this.CorreoUsuario = CorreoUsuario
        this.PasswordUsuario = PasswordUsuario
        this.FechaCreacionUsuario = FechaCreacionUsuario
        this.EstadoUsuario = EstadoUsuario
    }

    fun getNombreCompleto() : String{
        return this.NombreUsuario + " " + this.ApellidoPaternoUsuario + " " + this.ApellidoMaternoUsuario
    }

    fun getEdad() : String{
        var Edad = ""

        val FechaNacimientoUsuarioDate = LocalDate.parse(this.FechaNacimientoUsuario, DateTimeFormatter.ISO_DATE)

        Edad = Period.between(
            LocalDate.of(FechaNacimientoUsuarioDate.year, FechaNacimientoUsuarioDate.month, FechaNacimientoUsuarioDate.dayOfMonth),
            LocalDate.now()
        ).years.toString()

        return Edad
    }


    data class Builder(
        var IdUsuario: BigInteger? = null,
        var NombreUsuario:String? = null,
        var ApellidoPaternoUsuario:String? = null,
        var ApellidoMaternoUsuario: String? = null,
        var FechaNacimientoUsuario:String? = null,
        var EscolaridadUsuario:String? = null,
        var ProfesionUsuario:String? = null,
        var DescripcionUsuario:String? = null,
        var ImagenPerfilUsuario:String? = null,
        var CorreoUsuario: String? = null,
        var PasswordUsuario:String? = null,
        var FechaCreacionUsuario:String? = null,
        var EstadoUsuario:Byte? = null) {


        fun setIdUsuario(IdUsuario: BigInteger) = apply { this.IdUsuario = IdUsuario }
        fun setNombreUsuario(NombreUsuario: String) = apply { this.NombreUsuario = NombreUsuario }
        fun setApellidoPaternoUsuario(ApellidoPaternoUsuario: String) = apply { this.ApellidoPaternoUsuario = ApellidoPaternoUsuario }
        fun setApellidoMaternoUsuario(ApellidoMaternoUsuario: String) = apply { this.ApellidoMaternoUsuario = ApellidoMaternoUsuario }
        fun setFechaNacimientoUsuario(FechaNacimientoUsuario: String) = apply { this.FechaNacimientoUsuario = FechaNacimientoUsuario }
        fun setEscolaridadUsuario(EscolaridadUsuario: String) = apply { this.EscolaridadUsuario = EscolaridadUsuario }
        fun setProfesionUsuario(ProfesionUsuario: String) = apply { this.ProfesionUsuario = ProfesionUsuario }
        fun setDescripcionUsuario(DescripcionUsuario: String) = apply { this.DescripcionUsuario = DescripcionUsuario }
        fun setImagenPerfilUsuario(ImagenPerfilUsuario: String) = apply { this.ImagenPerfilUsuario = ImagenPerfilUsuario }
        fun setCorreoUsuario(CorreoUsuario: String) = apply { this.CorreoUsuario = CorreoUsuario }
        fun setPasswordUsuario(PasswordUsuario: String) = apply { this.PasswordUsuario = PasswordUsuario }
        fun setFechaCreacionUsuario(FechaCreacionUsuario: String) = apply { this.FechaCreacionUsuario = FechaCreacionUsuario }
        fun setEstadoUsuario(EstadoUsuario: Byte) = apply { this.EstadoUsuario = EstadoUsuario }


        fun build() = Usuarios_Model(IdUsuario, NombreUsuario, ApellidoPaternoUsuario, ApellidoMaternoUsuario,
                                    FechaNacimientoUsuario, EscolaridadUsuario, ProfesionUsuario, DescripcionUsuario,
                                    ImagenPerfilUsuario, CorreoUsuario, PasswordUsuario, FechaCreacionUsuario, EstadoUsuario)
    }

}


