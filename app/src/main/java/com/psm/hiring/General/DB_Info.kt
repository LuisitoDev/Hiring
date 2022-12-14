package com.psm.hiring.General

class DB_Info {

    //DECLARAMOS  EL NOMBRE Y VERSION DE TAL FOR QUE PUEDA SER VISIBLES PARA CUALQUIER CLASE
    companion object{
        val DB_NAME =  "hiring_db"
        val DB_VERSION =  1
    }

    //VAMOS ES DEFINIR EL ESQUEMA DE UNA DE LAS TABLAS
    abstract class tblUsuarios{
        //DEFINIMOS LOS ATRIBUTOS DE LA CLASE USANDO CONTANTES
        companion object{
            val TABLE_NAME = "usuarios"
            val Col_IdUsuario =  "IdUsuario"
            val Col_NombreUsuario =  "NombreUsuario"
            val Col_ApellidoPaternoUsuario = "ApellidoPaternoUsuario"
            val Col_ApellidoMaternoUsuario =  "ApellidoMaternoUsuario"
            val Col_FechaNacimientoUsuario =  "FechaNacimientoUsuario"
            val Col_EscolaridadUsuario =  "EscolaridadUsuario"
            val Col_ProfesionUsuario = "ProfesionUsuario"
            val Col_DescripcionUsuario =  "DescripcionUsuario"
            val Col_ImagenPerfilUsuario =  "ImagenPerfilUsuario"
            val Col_CorreoUsuario = "CorreoUsuario"
            val Col_PasswordUsuario =  "PasswordUsuario"
            val Col_FechaCreacionUsuario =  "FechaCreacionUsuario"
            val Col_EstadoUsuario =  "EstadoUsuario"
        }
    }

    abstract class tblTrabajos{
        companion object{
            val TABLE_NAME = "trabajos"
            val Col_IdTrabajo =  "IdTrabajo"
            val Col_TituloTrabajo =  "TituloTrabajo"
            val Col_DescripcionTrabajo = "DescripcionTrabajo"
            val Col_PagoTrabajo =  "PagoTrabajo"
            val Col_StatusTrabajo =  "StatusTrabajo"
            val Col_FechaCreacionTrabajo =  "FechaCreacionTrabajo"
            val Col_EstadoTrabajo = "EstadoTrabajo"
            val Col_UsuarioCreador =  "UsuarioCreador"
        }
    }


    abstract class tblArchivos{
        companion object{
            val TABLE_NAME = "archivos"
            val Col_IdArchivo =  "IdArchivo"
            val Col_ArchivoData =  "ArchivoData"
            val Col_TrabajoAsignado = "TrabajoAsignado"
        }
    }


    abstract class tblSolicitudes{
        companion object{
            val TABLE_NAME = "solicitudes"
            val Col_UsuarioSolicita =  "UsuarioSolicita"
            val Col_TrabajoSolicitado =  "TrabajoSolicitado"
            val Col_StatusSolicitud = "StatusSolicitud"
            val Col_FechaCreacionSolicitud =  "FechaCreacionSolicitud"
            val Col_FechaRespuestaSolicitud =  "FechaRespuestaSolicitud"
            val Col_EstadoSolicitud = "EstadoSolicitud"
        }
    }



    abstract class tblMensajes{
        companion object{
            val TABLE_NAME = "mensajes"
            val Col_IdMensaje =  "IdMensaje"
            val Col_UsuarioEnvia =  "UsuarioEnvia"
            val Col_UsuarioRecibe = "UsuarioRecibe"
            val Col_DescripcionMensaje =  "DescripcionMensaje"
            val Col_FechaCreacionMensaje =  "FechaCreacionMensaje"
            val Col_EstadoMensaje = "EstadoMensaje"
        }
    }

}