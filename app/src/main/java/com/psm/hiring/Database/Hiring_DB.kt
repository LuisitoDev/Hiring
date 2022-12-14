package com.psm.hiring.Database

import android.content.Context
import android.database.CursorWindow
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.psm.hiring.DAOs.*
import com.psm.hiring.General.DB_Info
import com.psm.hiring.Modelos.Archivos_Model
import com.psm.hiring.Modelos.Solicitudes_Model
import com.psm.hiring.Modelos.Trabajos_Model
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.Utils.DataManager
import java.lang.Exception
import java.lang.reflect.Field


class Hiring_DB (var context: Context): SQLiteOpenHelper(context,DB_Info.DB_NAME,null,DB_Info.DB_VERSION){

    private var dataBase:SQLiteDatabase = this.writableDatabase

    var daoUsuarios : Usuarios_DAO
    var daoTrabajos : Trabajos_DAO
    var daoArchivos : Archivos_DAO
    var daoSoliciutdes : Solicitudes_DAO
    var daoMensajes : Mensajes_DAO

    init{
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        daoUsuarios = Usuarios_DAO(context, dataBase)
        daoTrabajos = Trabajos_DAO(context, dataBase)
        daoArchivos = Archivos_DAO(context, dataBase)
        daoSoliciutdes = Solicitudes_DAO(context, dataBase)
        daoMensajes = Mensajes_DAO(context, dataBase)
    }

    override fun onCreate(db: SQLiteDatabase?){
        //SI NO EXISTE LA BASE DE DATOS LA CREA
        try{

            val createTableUsuarios:String =
                "CREATE TABLE ${DB_Info.tblUsuarios.TABLE_NAME} (" +
                        "  ${DB_Info.tblUsuarios.Col_IdUsuario}                   integer PRIMARY KEY," +
                        "  ${DB_Info.tblUsuarios.Col_NombreUsuario}               varchar(30) ," +
                        "  ${DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario}      varchar(30) ," +
                        "  ${DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario}      varchar(30) ," +
                        "  ${DB_Info.tblUsuarios.Col_FechaNacimientoUsuario}      varchar(30) ," +
                        "  ${DB_Info.tblUsuarios.Col_EscolaridadUsuario}          varchar(50) ," +
                        "  ${DB_Info.tblUsuarios.Col_ProfesionUsuario}            varchar(100) ," +
                        "  ${DB_Info.tblUsuarios.Col_DescripcionUsuario}          varchar(300) ," +
                        "  ${DB_Info.tblUsuarios.Col_ImagenPerfilUsuario}         mediumblob ," +
                        "  ${DB_Info.tblUsuarios.Col_CorreoUsuario}               varchar(60) ," +
                        "  ${DB_Info.tblUsuarios.Col_PasswordUsuario}             varchar(30) ," +
                        "  ${DB_Info.tblUsuarios.Col_FechaCreacionUsuario}        varchar(30) ," +
                        "  ${DB_Info.tblUsuarios.Col_EstadoUsuario}               integer DEFAULT '1'" +
                        ");"

            db?.execSQL(createTableUsuarios)

            val createTableTrabajos: String =
                "CREATE TABLE ${DB_Info.tblTrabajos.TABLE_NAME} (" +
                        "  ${DB_Info.tblTrabajos.Col_IdTrabajo}                   integer PRIMARY KEY," +
                        "  ${DB_Info.tblTrabajos.Col_TituloTrabajo}               varchar(100)," +
                        "  ${DB_Info.tblTrabajos.Col_DescripcionTrabajo}          varchar(500)," +
                        "  ${DB_Info.tblTrabajos.Col_PagoTrabajo}                 varchar(20)," +
                        "  ${DB_Info.tblTrabajos.Col_StatusTrabajo}               varchar(30)," +
                        "  ${DB_Info.tblTrabajos.Col_FechaCreacionTrabajo}        varchar(30)," +
                        "  ${DB_Info.tblTrabajos.Col_EstadoTrabajo}               integer DEFAULT '1'," +
                        "  ${DB_Info.tblTrabajos.Col_UsuarioCreador}              integer," +
                        "  CONSTRAINT FK_TRAB_USER" +
                        "    FOREIGN KEY (${DB_Info.tblTrabajos.Col_UsuarioCreador})" +
                        "    REFERENCES ${DB_Info.tblUsuarios.TABLE_NAME} (${DB_Info.tblUsuarios.Col_IdUsuario})" +
                        ") "
            db?.execSQL(createTableTrabajos)

            Log.e("ENTRO","CREO TABLAS")



            val createTableArchivos:String =
                "CREATE TABLE ${DB_Info.tblArchivos.TABLE_NAME} (" +
                        "  ${DB_Info.tblArchivos.Col_IdArchivo}                   integer PRIMARY KEY," +
                        "  ${DB_Info.tblArchivos.Col_ArchivoData}                 mediumblob," +
                        "  ${DB_Info.tblArchivos.Col_TrabajoAsignado}             integer," +
                        "  CONSTRAINT FK_ARCH_TRAB" +
                        "    FOREIGN KEY (${DB_Info.tblArchivos.Col_TrabajoAsignado})" +
                        "    REFERENCES ${DB_Info.tblTrabajos.TABLE_NAME}  (${DB_Info.tblTrabajos.Col_IdTrabajo})" +
                        "" +
                        ");"


            db?.execSQL(createTableArchivos)


            val createTableSolicitudes:String =
                "CREATE TABLE ${DB_Info.tblSolicitudes.TABLE_NAME} (" +
                        "  ${DB_Info.tblSolicitudes.Col_UsuarioSolicita}          integer," +
                        "  ${DB_Info.tblSolicitudes.Col_TrabajoSolicitado}        integer," +
                        "  ${DB_Info.tblSolicitudes.Col_StatusSolicitud}          varchar(30) DEFAULT 'En proceso'," +
                        "  ${DB_Info.tblSolicitudes.Col_FechaCreacionSolicitud}   varchar(30)," +
                        "  ${DB_Info.tblSolicitudes.Col_FechaRespuestaSolicitud}  varchar(30) DEFAULT NULL," +
                        "  ${DB_Info.tblSolicitudes.Col_EstadoSolicitud}          integer DEFAULT '1'," +
                        "" +
                        "   PRIMARY KEY (${DB_Info.tblSolicitudes.Col_UsuarioSolicita}, ${DB_Info.tblSolicitudes.Col_TrabajoSolicitado})," +
                        "" +
                        "  CONSTRAINT FK_SOLI_USER" +
                        "    FOREIGN KEY (${DB_Info.tblSolicitudes.Col_UsuarioSolicita})" +
                        "    REFERENCES ${DB_Info.tblUsuarios.TABLE_NAME} (${DB_Info.tblUsuarios.Col_IdUsuario})" +
                        "" +
                        "  CONSTRAINT FK_SOLI_TRAB" +
                        "    FOREIGN KEY (${DB_Info.tblSolicitudes.Col_TrabajoSolicitado})" +
                        "    REFERENCES ${DB_Info.tblTrabajos.TABLE_NAME} (${DB_Info.tblTrabajos.Col_IdTrabajo})" +
                        "" +
                        "); "

            db?.execSQL(createTableSolicitudes)


            val createTableMensajes:String =
                "CREATE TABLE ${DB_Info.tblMensajes.TABLE_NAME} (" +
                        "  ${DB_Info.tblMensajes.Col_IdMensaje}                   integer PRIMARY KEY," +
                        "  ${DB_Info.tblMensajes.Col_UsuarioRecibe}               integer," +
                        "  ${DB_Info.tblMensajes.Col_UsuarioEnvia}                integer," +
                        "  ${DB_Info.tblMensajes.Col_DescripcionMensaje}          varchar(800)," +
                        "  ${DB_Info.tblMensajes.Col_FechaCreacionMensaje}        varchar(30)," +
                        "  ${DB_Info.tblMensajes.Col_EstadoMensaje}               integer DEFAULT '1'," +
                        "" +
                        "  CONSTRAINT FK_MSJ_USER_ENV" +
                        "    FOREIGN KEY (${DB_Info.tblMensajes.Col_UsuarioEnvia})" +
                        "    REFERENCES ${DB_Info.tblUsuarios.TABLE_NAME} (${DB_Info.tblUsuarios.Col_IdUsuario})" +
                        "" +
                        "  CONSTRAINT FK_MSJ_USER_REC" +
                        "    FOREIGN KEY (${DB_Info.tblMensajes.Col_UsuarioRecibe})" +
                        "    REFERENCES ${DB_Info.tblUsuarios.TABLE_NAME} (${DB_Info.tblUsuarios.Col_IdUsuario})" +
                        "" +
                        "" +
                        ") "

            db?.execSQL(createTableMensajes)



            Log.e("ENTRO","CREO TABLAS")

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            //Toast.makeText(this.context, "Error crear tablas SQLite", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertarUsuarioInLogin(p_Usuario: Usuarios_Model){
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        dataBase = this.writableDatabase

        val usuarioIngresado = daoUsuarios.getUsuario(p_Usuario.IdUsuario!!)
        if (usuarioIngresado == null){
            daoUsuarios.insertUsuario(p_Usuario)
        }
        else {
            daoUsuarios.updateUsuario(p_Usuario)
        }

        //dataBase.close()
    }


    fun insertarUsuarioInfo(p_Usuario: Usuarios_Model){
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        dataBase = this.writableDatabase

        val usuarioIngresado = daoUsuarios.getUsuario(p_Usuario.IdUsuario!!)
        if (usuarioIngresado == null){
            daoUsuarios.insertUsuario(p_Usuario)
        }
        else {
            var usuario_Actualizar = p_Usuario

            usuario_Actualizar.NombreUsuario = if (!DataManager.isStringEmpty(p_Usuario.NombreUsuario)) p_Usuario.NombreUsuario else usuarioIngresado.NombreUsuario
             usuario_Actualizar.ApellidoPaternoUsuario = if (!DataManager.isStringEmpty(p_Usuario.ApellidoPaternoUsuario)) p_Usuario.ApellidoPaternoUsuario else usuarioIngresado.ApellidoPaternoUsuario
             usuario_Actualizar.ApellidoMaternoUsuario = if (!DataManager.isStringEmpty(p_Usuario.ApellidoMaternoUsuario)) p_Usuario.ApellidoMaternoUsuario else usuarioIngresado.ApellidoMaternoUsuario
             usuario_Actualizar.FechaNacimientoUsuario = if (!DataManager.isStringEmpty(p_Usuario.FechaNacimientoUsuario)) p_Usuario.FechaNacimientoUsuario else usuarioIngresado.FechaNacimientoUsuario
             usuario_Actualizar.EscolaridadUsuario = if (!DataManager.isStringEmpty(p_Usuario.EscolaridadUsuario)) p_Usuario.EscolaridadUsuario else usuarioIngresado.EscolaridadUsuario
             usuario_Actualizar.ProfesionUsuario = if (!DataManager.isStringEmpty(p_Usuario.ProfesionUsuario)) p_Usuario.ProfesionUsuario else usuarioIngresado.ProfesionUsuario
             usuario_Actualizar.DescripcionUsuario = if (!DataManager.isStringEmpty(p_Usuario.DescripcionUsuario)) p_Usuario.DescripcionUsuario else usuarioIngresado.DescripcionUsuario
             usuario_Actualizar.ImagenPerfilUsuario = if (!DataManager.isStringEmpty(p_Usuario.ImagenPerfilUsuario)) p_Usuario.ImagenPerfilUsuario else usuarioIngresado.ImagenPerfilUsuario
             usuario_Actualizar.CorreoUsuario = if (!DataManager.isStringEmpty(p_Usuario.CorreoUsuario)) p_Usuario.CorreoUsuario else usuarioIngresado.CorreoUsuario
             usuario_Actualizar.PasswordUsuario = if (!DataManager.isStringEmpty(p_Usuario.PasswordUsuario)) p_Usuario.PasswordUsuario else usuarioIngresado.PasswordUsuario
             usuario_Actualizar.FechaCreacionUsuario = if (!DataManager.isStringEmpty(p_Usuario.FechaCreacionUsuario)) p_Usuario.FechaCreacionUsuario else usuarioIngresado.FechaCreacionUsuario
             usuario_Actualizar.EstadoUsuario = if (p_Usuario.EstadoUsuario != null) p_Usuario.EstadoUsuario else usuarioIngresado.EstadoUsuario

            daoUsuarios.updateUsuario(usuario_Actualizar)
        }

        //dataBase.close()
    }

    fun insertarListaTrabajosInfo(ListaTrabajos: List<Trabajos_Model>){
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        dataBase = this.writableDatabase

        ListaTrabajos?.forEach { iTrabajo ->
            UserApplication.dbHelper.insertarTrabajoInfo(iTrabajo)
        }
        //dataBase.close()
    }

    private fun insertarTrabajoInfo(p_Trabajo: Trabajos_Model){
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        dataBase = this.writableDatabase

        val trabajoIngresado = daoTrabajos.getTrabajo(p_Trabajo.IdTrabajo!!)

        if (trabajoIngresado == null){
            daoTrabajos.insertTrabajo(p_Trabajo)
        }
        else {

            var trabajo_Actualizar = p_Trabajo

            trabajo_Actualizar.TituloTrabajo = if (!DataManager.isStringEmpty(p_Trabajo.TituloTrabajo)) p_Trabajo.TituloTrabajo else trabajoIngresado.TituloTrabajo
            trabajo_Actualizar.DescripcionTrabajo = if (!DataManager.isStringEmpty(p_Trabajo.DescripcionTrabajo)) p_Trabajo.DescripcionTrabajo else trabajoIngresado.DescripcionTrabajo
            trabajo_Actualizar.PagoTrabajo = if (!DataManager.isStringEmpty(p_Trabajo.PagoTrabajo)) p_Trabajo.PagoTrabajo else trabajoIngresado.PagoTrabajo
            trabajo_Actualizar.StatusTrabajo = if (!DataManager.isStringEmpty(p_Trabajo.StatusTrabajo)) p_Trabajo.StatusTrabajo else trabajoIngresado.StatusTrabajo
            trabajo_Actualizar.FechaCreacionTrabajo = if (!DataManager.isStringEmpty(p_Trabajo.FechaCreacionTrabajo)) p_Trabajo.FechaCreacionTrabajo else trabajoIngresado.FechaCreacionTrabajo
            trabajo_Actualizar.EstadoTrabajo = if (p_Trabajo.EstadoTrabajo != null) p_Trabajo.EstadoTrabajo else trabajoIngresado.EstadoTrabajo
            trabajo_Actualizar.UsuarioCreador = if (p_Trabajo.UsuarioCreador != null) p_Trabajo.UsuarioCreador else trabajoIngresado.UsuarioCreador

            trabajo_Actualizar.ListaArchivosModel = if (p_Trabajo.ListaArchivosModel?.size != 0) p_Trabajo.ListaArchivosModel else trabajoIngresado.ListaArchivosModel
            trabajo_Actualizar.UsuarioCreadorModel = if (p_Trabajo.UsuarioCreadorModel != null) p_Trabajo.UsuarioCreadorModel else trabajoIngresado.UsuarioCreadorModel
            trabajo_Actualizar.SolicitudAplicadaModel = if (p_Trabajo.SolicitudAplicadaModel != null) p_Trabajo.SolicitudAplicadaModel else trabajoIngresado.SolicitudAplicadaModel

            daoTrabajos.updateTrabajo(trabajo_Actualizar)
        }

        //dataBase.close()
    }


    fun crearTrabajoOfferJob(p_Trabajo: Trabajos_Model){
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        dataBase = this.writableDatabase

        val trabajoIngresado = daoTrabajos.getTrabajo(p_Trabajo.IdTrabajo!!)

        if (trabajoIngresado == null){
            daoTrabajos.insertTrabajo(p_Trabajo)
        }

        //dataBase.close()
    }


    fun insertarArchivoInfo(p_Archivo: Archivos_Model){
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        dataBase = this.writableDatabase

        val archivoIngresado = daoArchivos.getArchivo(p_Archivo.IdArchivo!!)
        if (archivoIngresado == null){
            daoArchivos.insertArchivo(p_Archivo)
        }
        else {
            var archivo_Actualizar = p_Archivo

            archivo_Actualizar.ArchivoData = if (!DataManager.isStringEmpty(p_Archivo.ArchivoData)) p_Archivo.ArchivoData else archivoIngresado.ArchivoData
            archivo_Actualizar.TrabajoAsignado = if (p_Archivo.TrabajoAsignado != null) p_Archivo.TrabajoAsignado else archivoIngresado.TrabajoAsignado

            daoArchivos.updateArchivo(archivo_Actualizar)
        }

        //dataBase.close()
    }



    fun insertarListaSolicitudesInfo(ListaSolicitudes: List<Solicitudes_Model>){
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        dataBase = this.writableDatabase

        ListaSolicitudes?.forEach { iSolicitud ->
            UserApplication.dbHelper.insertarSolicitudInfo(iSolicitud)
        }
        //dataBase.close()
    }

    fun insertarSolicitudInfo(p_Solicitud: Solicitudes_Model){
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        dataBase = this.writableDatabase

        val solicitudIngresada = daoSoliciutdes.getSolicitud(p_Solicitud.UsuarioSolicita!!, p_Solicitud.TrabajoSolicitado!!)
        if (solicitudIngresada == null){
            daoSoliciutdes.insertSolicitud(p_Solicitud)
        }
        else {
            var solicitud_Actualizar = p_Solicitud

            solicitud_Actualizar.UsuarioSolicita = if (p_Solicitud.UsuarioSolicita != null) p_Solicitud.UsuarioSolicita else solicitudIngresada.UsuarioSolicita
            solicitud_Actualizar.TrabajoSolicitado = if (p_Solicitud.TrabajoSolicitado != null) p_Solicitud.TrabajoSolicitado else solicitudIngresada.TrabajoSolicitado
            solicitud_Actualizar.StatusSolicitud = if (!DataManager.isStringEmpty(p_Solicitud.StatusSolicitud)) p_Solicitud.StatusSolicitud else solicitudIngresada.StatusSolicitud
            solicitud_Actualizar.FechaCreacionSolicitud = if (!DataManager.isStringEmpty(p_Solicitud.FechaCreacionSolicitud)) p_Solicitud.FechaCreacionSolicitud else solicitudIngresada.FechaCreacionSolicitud
            solicitud_Actualizar.FechaRespuestaSolicitud = if (!DataManager.isStringEmpty(p_Solicitud.FechaRespuestaSolicitud)) p_Solicitud.FechaRespuestaSolicitud else solicitudIngresada.FechaRespuestaSolicitud
            solicitud_Actualizar.EstadoSolicitud = if (p_Solicitud.EstadoSolicitud != null) p_Solicitud.EstadoSolicitud else solicitudIngresada.EstadoSolicitud
            solicitud_Actualizar.UsuarioSolicitaModel = if (p_Solicitud.UsuarioSolicitaModel != null) p_Solicitud.UsuarioSolicitaModel else solicitudIngresada.UsuarioSolicitaModel
            solicitud_Actualizar.UsuarioCreadorModel = if (p_Solicitud.UsuarioCreadorModel != null) p_Solicitud.UsuarioCreadorModel else solicitudIngresada.UsuarioCreadorModel

            daoSoliciutdes.updateSolicitud(solicitud_Actualizar)
        }

        //dataBase.close()
    }

    fun delete(db: SQLiteDatabase?){
        try{
            var dropExec = "" +
                    "drop table if exists ${DB_Info.tblUsuarios.TABLE_NAME}; "
                    "drop table if exists ${DB_Info.tblTrabajos.TABLE_NAME}; " +
                    "drop table if exists ${DB_Info.tblSolicitudes.TABLE_NAME};  " +
                    "drop table if exists ${DB_Info.tblArchivos.TABLE_NAME}; " +
                    "drop table if exists ${DB_Info.tblMensajes.TABLE_NAME}; "


            db?.execSQL(dropExec)

            Log.e("ENTRO","CREO TABLAS")

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            //Toast.makeText(this.context, "Error elimnar tablas SQLite", Toast.LENGTH_SHORT).show()
        }


    }


}

