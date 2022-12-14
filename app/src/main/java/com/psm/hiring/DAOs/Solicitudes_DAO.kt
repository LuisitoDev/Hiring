package com.psm.hiring.DAOs

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.psm.hiring.Database.UserApplication
import com.psm.hiring.General.DB_Info
import com.psm.hiring.Modelos.Solicitudes_Model
import java.math.BigInteger

class Solicitudes_DAO (var context: Context, var dataBase:SQLiteDatabase){

    public fun insertSolicitud(solicitud: Solicitudes_Model):Boolean{

        val valuesSolicitud: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_UsuarioSolicita,solicitud.UsuarioSolicita?.toLong())
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_TrabajoSolicitado,solicitud.TrabajoSolicitado?.toLong())
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_StatusSolicitud,solicitud.StatusSolicitud)
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_FechaCreacionSolicitud, solicitud.FechaCreacionSolicitud)
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_FechaRespuestaSolicitud,solicitud.FechaRespuestaSolicitud)
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_EstadoSolicitud,solicitud.EstadoSolicitud)


        try {
            val result =  dataBase.insert(
                DB_Info.tblSolicitudes.TABLE_NAME,
                null,
                valuesSolicitud)

            if (result == (0).toLong()) {
                val strMessage = "Failed"
            }
            else {
                val strMessage = "Success"

                if (solicitud.UsuarioCreadorModel?.IdUsuario != null)
                    UserApplication.dbHelper.insertarUsuarioInfo(solicitud.UsuarioCreadorModel!!)

            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error insertar Solicitud SQLite", Toast.LENGTH_SHORT).show()
            boolResult =  false
        }

        //dataBase.close()

        return boolResult
    }


    public fun updateSolicitud(solicitud:Solicitudes_Model):Boolean{

        val valuesSolicitud: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_UsuarioSolicita,solicitud.UsuarioSolicita?.toLong())
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_TrabajoSolicitado,solicitud.TrabajoSolicitado?.toLong())
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_StatusSolicitud,solicitud.StatusSolicitud)
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_FechaCreacionSolicitud, solicitud.FechaCreacionSolicitud)
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_FechaRespuestaSolicitud,solicitud.FechaRespuestaSolicitud)
        valuesSolicitud.put(DB_Info.tblSolicitudes.Col_EstadoSolicitud,solicitud.EstadoSolicitud)

        val where:String =  "${DB_Info.tblSolicitudes.Col_UsuarioSolicita} = ? " +
                "AND        ${DB_Info.tblSolicitudes.Col_TrabajoSolicitado} = ?"

        try{

            val result =  dataBase.update(
                DB_Info.tblSolicitudes.TABLE_NAME,
                valuesSolicitud,
                where,
                arrayOf(solicitud.UsuarioSolicita.toString(), solicitud.TrabajoSolicitado.toString()))

            if (result != -1 ) {
                val strMessage = "Success"
            }
            else {
                val strMessage = "Failed"

                if (solicitud.UsuarioCreadorModel?.IdUsuario != null)
                    UserApplication.dbHelper.insertarUsuarioInfo(solicitud.UsuarioCreadorModel!!)

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error editar Solicitud SQLite", Toast.LENGTH_SHORT).show()
        }

        //dataBase.close()
        return  boolResult
    }


    @SuppressLint("Range")
    public fun getListSolicitudes():MutableList<Solicitudes_Model>{
        val listaSolicitudes:MutableList<Solicitudes_Model> = ArrayList()

        val columns:Array<String> =  arrayOf(
            DB_Info.tblSolicitudes.Col_UsuarioSolicita,
            DB_Info.tblSolicitudes.Col_TrabajoSolicitado,
            DB_Info.tblSolicitudes.Col_StatusSolicitud,
            DB_Info.tblSolicitudes.Col_FechaCreacionSolicitud,
            DB_Info.tblSolicitudes.Col_FechaRespuestaSolicitud,
            DB_Info.tblSolicitudes.Col_EstadoSolicitud)

        val data =  dataBase.query(
            DB_Info.tblSolicitudes.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null)

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                val solicitud:Solicitudes_Model = Solicitudes_Model()
                //TODO: ESTO FUNCIONA CON "data.getString"
                solicitud.UsuarioSolicita = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_UsuarioSolicita)).toBigInteger()
                solicitud.TrabajoSolicitado = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_TrabajoSolicitado)).toBigInteger()
                solicitud.StatusSolicitud =  data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_StatusSolicitud)) ?: ""
                solicitud.FechaCreacionSolicitud = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_FechaCreacionSolicitud)) ?: ""
                solicitud.FechaRespuestaSolicitud =  data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_FechaRespuestaSolicitud)) ?: ""
                solicitud.EstadoSolicitud =  data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_EstadoSolicitud)).toByte()


                listaSolicitudes.add(solicitud)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  listaSolicitudes
    }


    @SuppressLint("Range")
    public fun getNotificacionesSolicitudes(IdUsuarioSolicita: BigInteger):MutableList<Solicitudes_Model>{
        val listaSolicitudes:MutableList<Solicitudes_Model> = ArrayList()

        val columns:Array<String> =  arrayOf(
            DB_Info.tblSolicitudes.Col_UsuarioSolicita,
            DB_Info.tblSolicitudes.Col_TrabajoSolicitado,
            DB_Info.tblSolicitudes.Col_StatusSolicitud,
            DB_Info.tblSolicitudes.Col_FechaCreacionSolicitud,
            DB_Info.tblSolicitudes.Col_FechaRespuestaSolicitud,
            DB_Info.tblSolicitudes.Col_EstadoSolicitud,
            DB_Info.tblUsuarios.Col_NombreUsuario,
            DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario,
            DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario,
            DB_Info.tblUsuarios.Col_ProfesionUsuario,
            DB_Info.tblUsuarios.Col_ImagenPerfilUsuario)


        val from:String = "${DB_Info.tblSolicitudes.TABLE_NAME}    " +
                "INNER JOIN ${DB_Info.tblTrabajos.TABLE_NAME}   " +
                "ON ${DB_Info.tblTrabajos.Col_IdTrabajo} = ${DB_Info.tblSolicitudes.Col_TrabajoSolicitado} " +
                "INNER JOIN ${DB_Info.tblUsuarios.TABLE_NAME}   " +
                "ON ${DB_Info.tblUsuarios.Col_IdUsuario} = ${DB_Info.tblTrabajos.Col_UsuarioCreador} "


        val where:String =  "${DB_Info.tblSolicitudes.Col_UsuarioSolicita} = ${IdUsuarioSolicita.toString()} " +
                "AND ${DB_Info.tblSolicitudes.Col_StatusSolicitud} != \"En proceso\"  " +
                "AND ${DB_Info.tblSolicitudes.Col_EstadoSolicitud} = 1 "

        val groupBy:String = "${DB_Info.tblSolicitudes.Col_UsuarioSolicita}, ${DB_Info.tblSolicitudes.Col_TrabajoSolicitado}"

        val data =  dataBase.query(
            from,
            columns,
            where,
            null,
            groupBy,
            null,
            null)

        if(data.moveToFirst()){

            do{
                val solicitud:Solicitudes_Model = Solicitudes_Model()

                solicitud.UsuarioSolicita = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_UsuarioSolicita)).toBigInteger()
                solicitud.TrabajoSolicitado = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_TrabajoSolicitado)).toBigInteger()
                solicitud.StatusSolicitud =  data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_StatusSolicitud)) ?: ""
                solicitud.FechaCreacionSolicitud = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_FechaCreacionSolicitud)) ?: ""
                solicitud.FechaRespuestaSolicitud =  data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_FechaRespuestaSolicitud)) ?: ""
                solicitud.EstadoSolicitud =  data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_EstadoSolicitud)).toByte()

                solicitud.UsuarioCreadorModel?.NombreUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_NombreUsuario)) ?: ""
                solicitud.UsuarioCreadorModel?.ApellidoPaternoUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario)) ?: ""
                solicitud.UsuarioCreadorModel?.ApellidoMaternoUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario)) ?: ""
                solicitud.UsuarioCreadorModel?.ProfesionUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ProfesionUsuario)) ?: ""
                solicitud.UsuarioCreadorModel?.ImagenPerfilUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ImagenPerfilUsuario)) ?: ""

                listaSolicitudes.add(solicitud)

            }while (data.moveToNext())

        }
        return  listaSolicitudes
    }


    @SuppressLint("Range")
    public fun getSolicitud(IdUsuarioSolicita:BigInteger, IdTrabajoSolicitado:BigInteger):Solicitudes_Model?{
        var solicitud:Solicitudes_Model? = null

        val columns:Array<String> =  arrayOf(
            DB_Info.tblSolicitudes.Col_UsuarioSolicita,
            DB_Info.tblSolicitudes.Col_TrabajoSolicitado,
            DB_Info.tblSolicitudes.Col_StatusSolicitud,
            DB_Info.tblSolicitudes.Col_FechaCreacionSolicitud,
            DB_Info.tblSolicitudes.Col_FechaRespuestaSolicitud,
            DB_Info.tblSolicitudes.Col_EstadoSolicitud)

        val where:String =  "${DB_Info.tblSolicitudes.Col_UsuarioSolicita} = ${IdUsuarioSolicita.toString()} " +
                "AND        ${DB_Info.tblSolicitudes.Col_TrabajoSolicitado} = ${IdTrabajoSolicitado.toString()}"

        val data =  dataBase.query(
            DB_Info.tblSolicitudes.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            null)

        if(data.moveToFirst()){
            solicitud = Solicitudes_Model()
            //TODO: ESTO FUNCIONA CON "data.getString"
            solicitud.UsuarioSolicita = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_UsuarioSolicita)).toBigInteger()
            solicitud.TrabajoSolicitado = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_TrabajoSolicitado)).toBigInteger()
            solicitud.StatusSolicitud =  data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_StatusSolicitud)) ?: ""
            solicitud.FechaCreacionSolicitud = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_FechaCreacionSolicitud)) ?: ""
            solicitud.FechaRespuestaSolicitud =  data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_FechaRespuestaSolicitud)) ?: ""
            solicitud.EstadoSolicitud =  data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_EstadoSolicitud)).toByte()


        }

        //dataBase.close()
        return solicitud
    }

    public fun deleteSolicitud(solicitud:Solicitudes_Model):Boolean{

        var boolResult:Boolean =  false
        try{

            val where:String =  "${DB_Info.tblSolicitudes.Col_UsuarioSolicita} = ? " +
                    "AND        ${DB_Info.tblSolicitudes.Col_TrabajoSolicitado} = ?"

            val _success = dataBase.delete(
                DB_Info.tblSolicitudes.TABLE_NAME,
                where,
                arrayOf(solicitud.UsuarioSolicita.toString(), solicitud.TrabajoSolicitado.toString()))
            //dataBase.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error eliminar Solicitud SQLite", Toast.LENGTH_SHORT).show()
        }

        return  boolResult
    }



}
