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
import com.psm.hiring.Modelos.Archivos_Model
import com.psm.hiring.Modelos.Trabajos_Model
import java.math.BigInteger
import android.database.CursorWindow
import java.lang.reflect.Field


class Trabajos_DAO (var context: Context, var dataBase:SQLiteDatabase){

    public fun insertTrabajo(trabajo: Trabajos_Model):Boolean{

        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size


        val valuesTrabajo: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesTrabajo.put(DB_Info.tblTrabajos.Col_IdTrabajo,trabajo.IdTrabajo?.toLong())
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_TituloTrabajo,trabajo.TituloTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_DescripcionTrabajo,trabajo.DescripcionTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_PagoTrabajo,trabajo.PagoTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_StatusTrabajo, trabajo.StatusTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_FechaCreacionTrabajo,trabajo.FechaCreacionTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_EstadoTrabajo,trabajo.EstadoTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_UsuarioCreador,trabajo.UsuarioCreador?.toLong())

        try {
            val result =  dataBase.insert(
                DB_Info.tblTrabajos.TABLE_NAME,
                null,
                valuesTrabajo)

            if (result == (0).toLong()) {
                val strMessage = "Failed"
            }
            else {
                val strMessage = "Success"

                if(trabajo.ListaArchivosModel?.size != 0) {
                    UserApplication.dbHelper.daoArchivos.limpiarArchivos(trabajo.IdTrabajo!!)

                    trabajo.ListaArchivosModel?.forEach { itArchivoModel ->
                        UserApplication.dbHelper.daoArchivos.insertArchivo(
                            itArchivoModel
                        )
                    }
                }

                if (trabajo.UsuarioCreadorModel?.IdUsuario != null)
                    UserApplication.dbHelper.insertarUsuarioInfo(trabajo.UsuarioCreadorModel!!)

                if (trabajo.SolicitudAplicadaModel?.UsuarioSolicita != null)
                    UserApplication.dbHelper.insertarSolicitudInfo(trabajo.SolicitudAplicadaModel!!)    

            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error insertar Trabajo SQLite", Toast.LENGTH_SHORT).show()
            boolResult =  false
        }

        //dataBase.close()

        return boolResult
    }


    public fun updateTrabajo(trabajo:Trabajos_Model):Boolean{

        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        val valuesTrabajo: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesTrabajo.put(DB_Info.tblTrabajos.Col_IdTrabajo,trabajo.IdTrabajo?.toLong())
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_TituloTrabajo,trabajo.TituloTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_DescripcionTrabajo,trabajo.DescripcionTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_PagoTrabajo,trabajo.PagoTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_StatusTrabajo, trabajo.StatusTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_FechaCreacionTrabajo,trabajo.FechaCreacionTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_EstadoTrabajo,trabajo.EstadoTrabajo)
        valuesTrabajo.put(DB_Info.tblTrabajos.Col_UsuarioCreador,trabajo.UsuarioCreador?.toLong())

        val where:String =  "${DB_Info.tblTrabajos.Col_IdTrabajo} = ?"

        try{

            val result =  dataBase.update(
                DB_Info.tblTrabajos.TABLE_NAME,
                valuesTrabajo,
                where,
                arrayOf(trabajo.IdTrabajo.toString()))

            if (result != -1 ) {
                val strMessage = "Success"

                if(trabajo.ListaArchivosModel?.size != 0) {
                    UserApplication.dbHelper.daoArchivos.limpiarArchivos(trabajo.IdTrabajo!!)

                    trabajo.ListaArchivosModel?.forEach { itArchivoModel ->
                        UserApplication.dbHelper.daoArchivos.insertArchivo(
                            itArchivoModel
                        )
                    }
                }

                if (trabajo.UsuarioCreadorModel?.IdUsuario != null)
                    UserApplication.dbHelper.insertarUsuarioInfo(trabajo.UsuarioCreadorModel!!)

                if (trabajo.SolicitudAplicadaModel?.UsuarioSolicita != null)
                    UserApplication.dbHelper.insertarSolicitudInfo(trabajo.SolicitudAplicadaModel!!)

            }
            else {
                val strMessage = "Failed"

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error editar Trabaj SQLite", Toast.LENGTH_SHORT).show()
        }


        return  boolResult
    }

    @SuppressLint("Range")
    public fun getListTrabajos():MutableList<Trabajos_Model>{
        val listaTrabajos:MutableList<Trabajos_Model> = ArrayList()

        val columns:Array<String> =  arrayOf(
            DB_Info.tblTrabajos.Col_IdTrabajo,
            DB_Info.tblTrabajos.Col_TituloTrabajo,
            DB_Info.tblTrabajos.Col_DescripcionTrabajo,
            DB_Info.tblTrabajos.Col_PagoTrabajo,
            DB_Info.tblTrabajos.Col_StatusTrabajo,
            DB_Info.tblTrabajos.Col_FechaCreacionTrabajo,
            DB_Info.tblTrabajos.Col_EstadoTrabajo,
            DB_Info.tblTrabajos.Col_UsuarioCreador)

        val data =  dataBase.query(
            DB_Info.tblTrabajos.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            DB_Info.tblTrabajos.Col_IdTrabajo + " ASC")

        if(data.moveToFirst()){

            do{
                val trabajo:Trabajos_Model = Trabajos_Model()

                trabajo.IdTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_IdTrabajo)).toBigInteger()
                trabajo.TituloTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_TituloTrabajo)) ?: ""
                trabajo.DescripcionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_DescripcionTrabajo)) ?: ""
                trabajo.PagoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_PagoTrabajo)) ?: ""
                trabajo.StatusTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_StatusTrabajo)) ?: ""
                trabajo.FechaCreacionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_FechaCreacionTrabajo)) ?: ""
                trabajo.EstadoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_EstadoTrabajo)).toByte()
                trabajo.UsuarioCreador = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_UsuarioCreador)).toBigInteger()

                listaTrabajos.add(trabajo)

            }while (data.moveToNext())

        }
        return  listaTrabajos
    }


    @SuppressLint("Range")
    public fun getListTrabajosHome():MutableList<Trabajos_Model>{
        val listaTrabajos:MutableList<Trabajos_Model> = ArrayList()

        val columns:Array<String> =  arrayOf(
            DB_Info.tblTrabajos.Col_IdTrabajo,
            DB_Info.tblTrabajos.Col_TituloTrabajo,
            DB_Info.tblTrabajos.Col_DescripcionTrabajo,
            DB_Info.tblTrabajos.Col_PagoTrabajo,
            DB_Info.tblTrabajos.Col_StatusTrabajo,
            DB_Info.tblTrabajos.Col_FechaCreacionTrabajo,
            DB_Info.tblTrabajos.Col_EstadoTrabajo,
            DB_Info.tblTrabajos.Col_UsuarioCreador,
            DB_Info.tblArchivos.Col_IdArchivo,
            DB_Info.tblArchivos.Col_ArchivoData,
            DB_Info.tblArchivos.Col_TrabajoAsignado,
            DB_Info.tblUsuarios.Col_IdUsuario,
            DB_Info.tblUsuarios.Col_NombreUsuario,
            DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario,
            DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario,
            DB_Info.tblUsuarios.Col_ImagenPerfilUsuario,
            DB_Info.tblUsuarios.Col_ProfesionUsuario)

        val from:String = "${DB_Info.tblTrabajos.TABLE_NAME}    " +
                "INNER JOIN ${DB_Info.tblArchivos.TABLE_NAME}   " +
                "ON ${DB_Info.tblArchivos.Col_TrabajoAsignado} = ${DB_Info.tblTrabajos.Col_IdTrabajo} " +
                "INNER JOIN ${DB_Info.tblUsuarios.TABLE_NAME}   " +
                "ON ${DB_Info.tblUsuarios.Col_IdUsuario} = ${DB_Info.tblTrabajos.Col_UsuarioCreador} "

        val where:String =  "${DB_Info.tblTrabajos.Col_StatusTrabajo} = \"En proceso\" AND ${DB_Info.tblTrabajos.Col_EstadoTrabajo} = 1"

        val orderBy = "Case when ${DB_Info.tblTrabajos.Col_IdTrabajo} < 0 then 0 else 1 end, ${DB_Info.tblTrabajos.Col_IdTrabajo} desc"

        val data =  dataBase.query(
            from,
            columns,
            where,
            null,
            DB_Info.tblTrabajos.Col_IdTrabajo,
            null,
            orderBy)

        if(data.moveToFirst()){

            do{
                val trabajo:Trabajos_Model = Trabajos_Model()
                trabajo.IdTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_IdTrabajo)).toBigInteger()
                trabajo.TituloTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_TituloTrabajo)) ?: ""
                trabajo.DescripcionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_DescripcionTrabajo)) ?: ""
                trabajo.PagoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_PagoTrabajo)) ?: ""
                trabajo.StatusTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_StatusTrabajo)) ?: ""
                trabajo.FechaCreacionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_FechaCreacionTrabajo)) ?: ""
                trabajo.EstadoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_EstadoTrabajo)).toByte()
                trabajo.UsuarioCreador = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_UsuarioCreador)).toBigInteger()

                val ArchivosModelTrabajo = Archivos_Model()

                ArchivosModelTrabajo.IdArchivo = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_IdArchivo)).toBigInteger()
                ArchivosModelTrabajo.ArchivoData = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_ArchivoData)) ?: ""
                ArchivosModelTrabajo.TrabajoAsignado = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_TrabajoAsignado)).toBigInteger()

                trabajo.ListaArchivosModel?.add(ArchivosModelTrabajo)

                trabajo.UsuarioCreadorModel?.IdUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_IdUsuario)).toBigInteger()
                trabajo.UsuarioCreadorModel?.NombreUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_NombreUsuario)) ?: ""
                trabajo.UsuarioCreadorModel?.ApellidoPaternoUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario)) ?: ""
                trabajo.UsuarioCreadorModel?.ApellidoMaternoUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario)) ?: ""
                trabajo.UsuarioCreadorModel?.ImagenPerfilUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ImagenPerfilUsuario)) ?: ""
                trabajo.UsuarioCreadorModel?.ProfesionUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ProfesionUsuario)) ?: ""

                listaTrabajos.add(trabajo)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  listaTrabajos
    }



    @SuppressLint("Range")
    public fun getListMisTrabajos(IdUsuarioCreador:BigInteger):MutableList<Trabajos_Model>{
        val listaTrabajos:MutableList<Trabajos_Model> = ArrayList()
        

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE?
        val columns:Array<String> =  arrayOf(
            DB_Info.tblTrabajos.Col_IdTrabajo,
            DB_Info.tblTrabajos.Col_TituloTrabajo,
            DB_Info.tblTrabajos.Col_DescripcionTrabajo,
            DB_Info.tblTrabajos.Col_PagoTrabajo,
            DB_Info.tblTrabajos.Col_StatusTrabajo,
            DB_Info.tblTrabajos.Col_FechaCreacionTrabajo,
            DB_Info.tblTrabajos.Col_EstadoTrabajo,
            DB_Info.tblTrabajos.Col_UsuarioCreador,
            DB_Info.tblArchivos.Col_IdArchivo,
            DB_Info.tblArchivos.Col_ArchivoData,
            DB_Info.tblArchivos.Col_TrabajoAsignado)

        val from:String = "${DB_Info.tblTrabajos.TABLE_NAME}    " +
                "INNER JOIN ${DB_Info.tblArchivos.TABLE_NAME}   " +
                "ON ${DB_Info.tblArchivos.Col_TrabajoAsignado} = ${DB_Info.tblTrabajos.Col_IdTrabajo} "

        val where:String =  "${DB_Info.tblTrabajos.Col_UsuarioCreador} =  $IdUsuarioCreador     " +
                            "AND ${DB_Info.tblTrabajos.Col_EstadoTrabajo} = 1"

        val orderBy = "Case when ${DB_Info.tblTrabajos.Col_IdTrabajo} < 0 then 0 else 1 end, ${DB_Info.tblTrabajos.Col_IdTrabajo} desc"

        val data =  dataBase.query(
            from,
            columns,
            where,
            null,
            DB_Info.tblTrabajos.Col_IdTrabajo,
            null,
            orderBy)

        if(data.moveToFirst()){

            do{
                val trabajo:Trabajos_Model = Trabajos_Model()

                trabajo.IdTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_IdTrabajo)).toBigInteger()
                trabajo.TituloTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_TituloTrabajo)) ?: ""
                trabajo.DescripcionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_DescripcionTrabajo)) ?: ""
                trabajo.PagoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_PagoTrabajo)) ?: ""
                trabajo.StatusTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_StatusTrabajo)) ?: ""
                trabajo.FechaCreacionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_FechaCreacionTrabajo)) ?: ""
                trabajo.EstadoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_EstadoTrabajo)).toByte()
                trabajo.UsuarioCreador = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_UsuarioCreador)).toBigInteger()

                val ArchivosModelTrabajo = Archivos_Model()

                ArchivosModelTrabajo.IdArchivo = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_IdArchivo)).toBigInteger()
                ArchivosModelTrabajo.ArchivoData = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_ArchivoData)) ?: ""
                ArchivosModelTrabajo.TrabajoAsignado = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_TrabajoAsignado)).toBigInteger()

                trabajo.ListaArchivosModel?.add(ArchivosModelTrabajo)

                listaTrabajos.add(trabajo)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  listaTrabajos
    }


    @SuppressLint("Range")
    public fun getListMisTrabajosSolicitados(IdUsuarioSolicita:BigInteger):MutableList<Trabajos_Model>{
        val listaTrabajos:MutableList<Trabajos_Model> = ArrayList()


        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE?
        val columns:Array<String> =  arrayOf(
            DB_Info.tblTrabajos.Col_IdTrabajo,
            DB_Info.tblTrabajos.Col_TituloTrabajo,
            DB_Info.tblTrabajos.Col_DescripcionTrabajo,
            DB_Info.tblTrabajos.Col_PagoTrabajo,
            DB_Info.tblTrabajos.Col_StatusTrabajo,
            DB_Info.tblTrabajos.Col_FechaCreacionTrabajo,
            DB_Info.tblTrabajos.Col_EstadoTrabajo,
            DB_Info.tblTrabajos.Col_UsuarioCreador,
            DB_Info.tblArchivos.Col_IdArchivo,
            DB_Info.tblArchivos.Col_ArchivoData,
            DB_Info.tblArchivos.Col_TrabajoAsignado,
            DB_Info.tblSolicitudes.Col_UsuarioSolicita,
            DB_Info.tblSolicitudes.Col_TrabajoSolicitado,
            DB_Info.tblSolicitudes.Col_StatusSolicitud
        )

        val from:String = "${DB_Info.tblTrabajos.TABLE_NAME}    " +
                "INNER JOIN ${DB_Info.tblArchivos.TABLE_NAME}   " +
                "ON ${DB_Info.tblArchivos.Col_TrabajoAsignado} = ${DB_Info.tblTrabajos.Col_IdTrabajo} " +
                "INNER JOIN ${DB_Info.tblSolicitudes.TABLE_NAME}   " +
                "ON ${DB_Info.tblSolicitudes.Col_TrabajoSolicitado} = ${DB_Info.tblTrabajos.Col_IdTrabajo} "

        val where:String =  "${DB_Info.tblSolicitudes.Col_UsuarioSolicita} =  $IdUsuarioSolicita     " +
                "AND ${DB_Info.tblSolicitudes.Col_StatusSolicitud} = \"En proceso\"   "
                "AND ${DB_Info.tblTrabajos.Col_EstadoTrabajo} = 1   "

        val orderBy = "Case when ${DB_Info.tblTrabajos.Col_IdTrabajo} < 0 then 0 else 1 end, ${DB_Info.tblTrabajos.Col_IdTrabajo} desc"

        val data =  dataBase.query(
            from,
            columns,
            where,
            null,
            DB_Info.tblTrabajos.Col_IdTrabajo,
            null,
            orderBy)

        if(data.moveToFirst()){

            do{
                val trabajo:Trabajos_Model = Trabajos_Model()

                trabajo.IdTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_IdTrabajo)).toBigInteger()
                trabajo.TituloTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_TituloTrabajo)) ?: ""
                trabajo.DescripcionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_DescripcionTrabajo)) ?: ""
                trabajo.PagoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_PagoTrabajo)) ?: ""
                trabajo.StatusTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_StatusTrabajo)) ?: ""
                trabajo.FechaCreacionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_FechaCreacionTrabajo)) ?: ""
                trabajo.EstadoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_EstadoTrabajo)).toByte()
                trabajo.UsuarioCreador = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_UsuarioCreador)).toBigInteger()

                val ArchivosModelTrabajo = Archivos_Model()

                ArchivosModelTrabajo.IdArchivo = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_IdArchivo)).toBigInteger()
                ArchivosModelTrabajo.ArchivoData = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_ArchivoData)) ?: ""
                ArchivosModelTrabajo.TrabajoAsignado = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_TrabajoAsignado)).toBigInteger()

                trabajo.ListaArchivosModel?.add(ArchivosModelTrabajo)


                trabajo.SolicitudAplicadaModel?.UsuarioSolicita = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_UsuarioSolicita)).toBigInteger()
                trabajo.SolicitudAplicadaModel?.TrabajoSolicitado = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_TrabajoSolicitado)).toBigInteger()
                trabajo.SolicitudAplicadaModel?.StatusSolicitud = data.getString(data.getColumnIndex(DB_Info.tblSolicitudes.Col_StatusSolicitud)) ?: ""

                listaTrabajos.add(trabajo)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  listaTrabajos
    }


    @SuppressLint("Range")
    public fun getListMisTrabajosLocales(IdUsuarioCreador: BigInteger):MutableList<Trabajos_Model>{
        val listaTrabajos:MutableList<Trabajos_Model> = ArrayList()
        

        val columns:Array<String> =  arrayOf(
            DB_Info.tblTrabajos.Col_IdTrabajo,
            DB_Info.tblTrabajos.Col_TituloTrabajo,
            DB_Info.tblTrabajos.Col_DescripcionTrabajo,
            DB_Info.tblTrabajos.Col_PagoTrabajo,
            DB_Info.tblTrabajos.Col_StatusTrabajo,
            DB_Info.tblTrabajos.Col_FechaCreacionTrabajo,
            DB_Info.tblTrabajos.Col_EstadoTrabajo,
            DB_Info.tblTrabajos.Col_UsuarioCreador
        )

        val from:String = "${DB_Info.tblTrabajos.TABLE_NAME}    "

        val where:String =  "${DB_Info.tblTrabajos.Col_IdTrabajo} <=  -1     "

        val data =  dataBase.query(
            from,
            columns,
            where,
            null,
            DB_Info.tblTrabajos.Col_IdTrabajo,
            null,
            DB_Info.tblTrabajos.Col_IdTrabajo + " ASC")

        if(data.moveToFirst()){

            do{
                val trabajo:Trabajos_Model = Trabajos_Model()

                trabajo.IdTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_IdTrabajo)).toBigInteger()
                trabajo.TituloTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_TituloTrabajo)) ?: ""
                trabajo.DescripcionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_DescripcionTrabajo)) ?: ""
                trabajo.PagoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_PagoTrabajo)) ?: ""
                trabajo.StatusTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_StatusTrabajo)) ?: ""
                trabajo.FechaCreacionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_FechaCreacionTrabajo)) ?: ""
                trabajo.EstadoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_EstadoTrabajo)).toByte()
                trabajo.UsuarioCreador = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_UsuarioCreador)).toBigInteger()


                trabajo.ListaArchivosModel = UserApplication.dbHelper.daoArchivos.getListArchivosTrabajo(trabajo.IdTrabajo!!)

                listaTrabajos.add(trabajo)

            }while (data.moveToNext())

        }
        return  listaTrabajos
    }

    
    @SuppressLint("Range")
    public fun getTrabajo(IdTrabajo:BigInteger):Trabajos_Model?{
        var trabajo:Trabajos_Model? = null

        val columns:Array<String> =  arrayOf(
            DB_Info.tblTrabajos.Col_IdTrabajo,
            DB_Info.tblTrabajos.Col_TituloTrabajo,
            DB_Info.tblTrabajos.Col_DescripcionTrabajo,
            DB_Info.tblTrabajos.Col_PagoTrabajo,
            DB_Info.tblTrabajos.Col_StatusTrabajo,
            DB_Info.tblTrabajos.Col_FechaCreacionTrabajo,
            DB_Info.tblTrabajos.Col_EstadoTrabajo,
            DB_Info.tblTrabajos.Col_UsuarioCreador)

        val where:String =  "${DB_Info.tblTrabajos.Col_IdTrabajo} = ${IdTrabajo.toString()}"

        val data =  dataBase.query(
            DB_Info.tblTrabajos.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            DB_Info.tblTrabajos.Col_IdTrabajo + " DESC")

        if(data.moveToFirst()){
            trabajo = Trabajos_Model()
            //TODO: ESTO FUNCIONA CON "data.getString"
            trabajo.IdTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_IdTrabajo)).toBigInteger()
            trabajo.TituloTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_TituloTrabajo)) ?: ""
            trabajo.DescripcionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_DescripcionTrabajo)) ?: ""
            trabajo.PagoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_PagoTrabajo)) ?: ""
            trabajo.StatusTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_StatusTrabajo)) ?: ""
            trabajo.FechaCreacionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_FechaCreacionTrabajo)) ?: ""
            trabajo.EstadoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_EstadoTrabajo)).toByte()
            trabajo.UsuarioCreador = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_UsuarioCreador)).toBigInteger()
        }

        //dataBase.close()
        return trabajo
    }


    @SuppressLint("Range")
    public fun getLowerIdTrabajo():BigInteger?{
        var trabajo:Trabajos_Model? = null


        val columns:Array<String> =  arrayOf(
            DB_Info.tblTrabajos.Col_IdTrabajo)

        val where:String =  "${DB_Info.tblTrabajos.Col_IdTrabajo} <= -1"

        val data =  dataBase.query(
            DB_Info.tblTrabajos.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            DB_Info.tblTrabajos.Col_IdTrabajo + " ASC LIMIT 1")

        var IdTrabajoResult : BigInteger?

        if(data.moveToFirst()){
            IdTrabajoResult = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_IdTrabajo)).toBigInteger()
            IdTrabajoResult -= BigInteger("1")

        }
        else{
            IdTrabajoResult = BigInteger("-1")
        }

        //dataBase.close()
        return IdTrabajoResult
    }

    @SuppressLint("Range")
    public fun getDetallesTrabajo(IdTrabajo:BigInteger):Trabajos_Model?{
        var trabajo:Trabajos_Model? = null


        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE?
        val columns:Array<String> =  arrayOf(
            DB_Info.tblTrabajos.Col_IdTrabajo,
            DB_Info.tblTrabajos.Col_TituloTrabajo,
            DB_Info.tblTrabajos.Col_DescripcionTrabajo,
            DB_Info.tblTrabajos.Col_PagoTrabajo,
            DB_Info.tblTrabajos.Col_StatusTrabajo,
            DB_Info.tblTrabajos.Col_FechaCreacionTrabajo,
            DB_Info.tblTrabajos.Col_EstadoTrabajo,
            DB_Info.tblTrabajos.Col_UsuarioCreador)

        val where:String =  "${DB_Info.tblTrabajos.Col_IdTrabajo} = ${IdTrabajo.toString()}"

        val data =  dataBase.query(
            DB_Info.tblTrabajos.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            DB_Info.tblTrabajos.Col_IdTrabajo + " DESC")

        if(data.moveToFirst()){
            trabajo = Trabajos_Model()
            //TODO: ESTO FUNCIONA CON "data.getString"
            trabajo.IdTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_IdTrabajo)).toBigInteger()
            trabajo.TituloTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_TituloTrabajo)) ?: ""
            trabajo.DescripcionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_DescripcionTrabajo)) ?: ""
            trabajo.PagoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_PagoTrabajo)) ?: ""
            trabajo.StatusTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_StatusTrabajo)) ?: ""
            trabajo.FechaCreacionTrabajo = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_FechaCreacionTrabajo)) ?: ""
            trabajo.EstadoTrabajo =  data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_EstadoTrabajo)).toByte()
            trabajo.UsuarioCreador = data.getString(data.getColumnIndex(DB_Info.tblTrabajos.Col_UsuarioCreador)).toBigInteger()

            trabajo.ListaArchivosModel = UserApplication.dbHelper.daoArchivos.getListArchivosTrabajo(trabajo.IdTrabajo!!)

            trabajo.UsuarioCreadorModel = UserApplication.dbHelper.daoUsuarios.getUsuarioCreadorTrabajo(trabajo.UsuarioCreador!!)
        }

        //dataBase.close()
        return trabajo
    }


    public fun deleteTrabajo(IdTrabajo:BigInteger):Boolean{

        var boolResult:Boolean =  false
        try{

            val where:String =  "${DB_Info.tblTrabajos.Col_IdTrabajo} = ?"
            val _success = dataBase.delete(
                DB_Info.tblTrabajos.TABLE_NAME,
                where,
                arrayOf(IdTrabajo.toString()))
            //dataBase.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error eliminar Trabajo SQLite", Toast.LENGTH_SHORT).show()
        }

        return  boolResult
    }


}
