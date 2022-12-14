package com.psm.hiring.DAOs

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.psm.hiring.General.DB_Info
import com.psm.hiring.Modelos.Mensajes_Model
import java.math.BigInteger

class Mensajes_DAO (var context: Context, var dataBase:SQLiteDatabase){

    public fun insertMensaje(mensaje: Mensajes_Model):Boolean{

        val valuesMensaje: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesMensaje.put(DB_Info.tblMensajes.Col_IdMensaje,mensaje.IdMensaje?.toLong()) //TODO: ESTO FUNCIONA?
        valuesMensaje.put(DB_Info.tblMensajes.Col_UsuarioEnvia,mensaje.UsuarioEnvia?.toLong()) //TODO: ESTO FUNCIONA?
        valuesMensaje.put(DB_Info.tblMensajes.Col_UsuarioRecibe, mensaje.UsuarioRecibe?.toLong()) //TODO: ESTO FUNCIONA?
        valuesMensaje.put(DB_Info.tblMensajes.Col_DescripcionMensaje,mensaje.DescripcionMensaje)
        valuesMensaje.put(DB_Info.tblMensajes.Col_FechaCreacionMensaje,mensaje.FechaCreacionMensaje)
        valuesMensaje.put(DB_Info.tblMensajes.Col_EstadoMensaje,mensaje.EstadoMensaje)

        try {
            val result =  dataBase.insert(
                DB_Info.tblMensajes.TABLE_NAME,
                null,
                valuesMensaje)

            if (result == (0).toLong()) {
                val strMessage = "Failed"
            }
            else {
                val strMessage = "Success"
            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error insertar mensaje SQLite", Toast.LENGTH_SHORT).show()
            boolResult =  false
        }

        //dataBase.close()

        return boolResult
    }

    @SuppressLint("Range")
    public fun getListMensajes():MutableList<Mensajes_Model>{
        val listaMensajes:MutableList<Mensajes_Model> = ArrayList()

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE?
        val columns:Array<String> =  arrayOf(
            DB_Info.tblMensajes.Col_IdMensaje,
            DB_Info.tblMensajes.Col_UsuarioEnvia,
            DB_Info.tblMensajes.Col_UsuarioRecibe,
            DB_Info.tblMensajes.Col_DescripcionMensaje,
            DB_Info.tblMensajes.Col_FechaCreacionMensaje,
            DB_Info.tblMensajes.Col_EstadoMensaje)

        val data =  dataBase.query(
            DB_Info.tblMensajes.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            DB_Info.tblMensajes.Col_IdMensaje + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                val mensaje:Mensajes_Model = Mensajes_Model()
                //TODO: ESTO FUNCIONA CON "data.getString"
                mensaje.IdMensaje = data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_IdMensaje)).toBigInteger()
                mensaje.UsuarioEnvia =  data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_UsuarioRecibe)).toBigInteger()
                mensaje.UsuarioRecibe =  data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_UsuarioEnvia)).toBigInteger()
                mensaje.DescripcionMensaje = data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_DescripcionMensaje)).toString()
                mensaje.FechaCreacionMensaje = data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_FechaCreacionMensaje)).toString()
                mensaje.EstadoMensaje =  data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_EstadoMensaje)).toByte()

                listaMensajes.add(mensaje)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  listaMensajes
    }

    @SuppressLint("Range")
    public fun getMensaje(IdMensaje:BigInteger):Mensajes_Model?{
        var mensaje:Mensajes_Model? = null

        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE?
        val columns:Array<String> =  arrayOf(
            DB_Info.tblMensajes.Col_IdMensaje,
            DB_Info.tblMensajes.Col_UsuarioEnvia,
            DB_Info.tblMensajes.Col_UsuarioRecibe,
            DB_Info.tblMensajes.Col_DescripcionMensaje,
            DB_Info.tblMensajes.Col_FechaCreacionMensaje,
            DB_Info.tblMensajes.Col_EstadoMensaje)

        val where:String =  "${DB_Info.tblMensajes.Col_IdMensaje} = ${IdMensaje.toString()}"

        val data =  dataBase.query(
            DB_Info.tblMensajes.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            DB_Info.tblMensajes.Col_IdMensaje + " ASC")

        if(data.moveToFirst()){
            mensaje = Mensajes_Model()
            //TODO: ESTO FUNCIONA CON "data.getString"
            mensaje.IdMensaje = data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_IdMensaje)).toBigInteger()
            mensaje.UsuarioEnvia =  data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_UsuarioRecibe)).toBigInteger()
            mensaje.UsuarioRecibe =  data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_UsuarioEnvia)).toBigInteger()
            mensaje.DescripcionMensaje = data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_DescripcionMensaje)).toString()
            mensaje.FechaCreacionMensaje = data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_FechaCreacionMensaje)).toString()
            mensaje.EstadoMensaje =  data.getString(data.getColumnIndex(DB_Info.tblMensajes.Col_EstadoMensaje)).toByte()


        }

        //dataBase.close()
        return mensaje
    }

    public fun updateMensaje(mensaje:Mensajes_Model):Boolean{

        val valuesMensaje: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesMensaje.put(DB_Info.tblMensajes.Col_IdMensaje,mensaje.IdMensaje?.toLong()) //TODO: ESTO FUNCIONA?
        valuesMensaje.put(DB_Info.tblMensajes.Col_UsuarioEnvia,mensaje.UsuarioEnvia?.toLong()) //TODO: ESTO FUNCIONA?
        valuesMensaje.put(DB_Info.tblMensajes.Col_UsuarioRecibe, mensaje.UsuarioRecibe?.toLong()) //TODO: ESTO FUNCIONA?
        valuesMensaje.put(DB_Info.tblMensajes.Col_DescripcionMensaje,mensaje.DescripcionMensaje)
        valuesMensaje.put(DB_Info.tblMensajes.Col_FechaCreacionMensaje,mensaje.FechaCreacionMensaje)
        valuesMensaje.put(DB_Info.tblMensajes.Col_EstadoMensaje,mensaje.EstadoMensaje)

        val where:String =  "${DB_Info.tblMensajes.Col_IdMensaje} = ?"

        //val where2:String = DB_Info.tblMensajes.COL_IDGENRE  + "=?" + "AND" + DB_Info.tblMensajes.COL_TITLE + "=?"
        //arrayOf(1,"HOLA")

        try{

            val result =  dataBase.update(
                DB_Info.tblMensajes.TABLE_NAME,
                valuesMensaje,
                where,
                arrayOf(mensaje.IdMensaje.toString()))

            if (result != -1 ) {
                val strMessage = "Success"
            }
            else {
                val strMessage = "Failed"

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error editar mensaje SQLite", Toast.LENGTH_SHORT).show()
        }

        //dataBase.close()
        return  boolResult
    }

    public fun deleteMensaje(IdMensaje:BigInteger):Boolean{

        var boolResult:Boolean =  false
        try{

            val where:String =  "${DB_Info.tblMensajes.Col_IdMensaje} = ?"
            val _success = dataBase.delete(
                DB_Info.tblMensajes.TABLE_NAME,
                where,
                arrayOf(IdMensaje.toString()))
            //dataBase.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error eliminar mensaje SQLite", Toast.LENGTH_SHORT).show()
        }

        return  boolResult
    }


}
