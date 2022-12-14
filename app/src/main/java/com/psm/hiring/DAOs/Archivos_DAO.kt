package com.psm.hiring.DAOs

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.CursorWindow
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.psm.hiring.General.DB_Info
import com.psm.hiring.Modelos.Archivos_Model
import java.lang.reflect.Field
import java.math.BigInteger

class Archivos_DAO (var context: Context, var dataBase:SQLiteDatabase){

    public fun insertArchivo(archivo: Archivos_Model):Boolean{

        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        val valuesArchivo: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesArchivo.put(DB_Info.tblArchivos.Col_IdArchivo,archivo.IdArchivo?.toLong())
        valuesArchivo.put(DB_Info.tblArchivos.Col_ArchivoData,archivo.ArchivoData)
        valuesArchivo.put(DB_Info.tblArchivos.Col_TrabajoAsignado,archivo.TrabajoAsignado?.toLong())

        try {
            val result =  dataBase.insert(
                DB_Info.tblArchivos.TABLE_NAME,
                null,
                valuesArchivo)

            if (result == (0).toLong()) {
                val strMessage = "Failed"
            }
            else {
                val strMessage = "Success"
            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
            Toast.makeText(this.context, "Error crear  archivo SQLite", Toast.LENGTH_SHORT).show()
        }

        //dataBase.close()

        return boolResult
    }


    @SuppressLint("Range")
    public fun getListarchivos():MutableList<Archivos_Model>{
        val listaArchivos:MutableList<Archivos_Model> = ArrayList()



        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE?
        val columns:Array<String> =  arrayOf(
            DB_Info.tblArchivos.Col_IdArchivo,
            DB_Info.tblArchivos.Col_ArchivoData,
            DB_Info.tblArchivos.Col_TrabajoAsignado)

        val data =  dataBase.query(
            DB_Info.tblArchivos.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            DB_Info.tblArchivos.Col_IdArchivo + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                val archivo:Archivos_Model = Archivos_Model()
                //TODO: ESTO FUNCIONA CON "data.getString"
                archivo.IdArchivo = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_IdArchivo)).toBigInteger()
                archivo.ArchivoData =  data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_ArchivoData)) ?: ""
                archivo.TrabajoAsignado = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_TrabajoAsignado)).toBigInteger()

                listaArchivos.add(archivo)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  listaArchivos
    }
    
    @SuppressLint("Range")
    public fun getListArchivosTrabajo(IdTrabajo: BigInteger):MutableList<Archivos_Model>{
        val listaArchivos:MutableList<Archivos_Model> = ArrayList()



        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE?
        val columns:Array<String> =  arrayOf(
            DB_Info.tblArchivos.Col_IdArchivo,
            DB_Info.tblArchivos.Col_ArchivoData,
            DB_Info.tblArchivos.Col_TrabajoAsignado)

        val where:String =  "${DB_Info.tblArchivos.Col_TrabajoAsignado} = ${IdTrabajo.toString()}"

        val data =  dataBase.query(
            DB_Info.tblArchivos.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            DB_Info.tblArchivos.Col_IdArchivo + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                val archivo:Archivos_Model = Archivos_Model()
                //TODO: ESTO FUNCIONA CON "data.getString"
                archivo.IdArchivo = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_IdArchivo)).toBigInteger()
                archivo.ArchivoData =  data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_ArchivoData)) ?: ""
                archivo.TrabajoAsignado = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_TrabajoAsignado)).toBigInteger()

                listaArchivos.add(archivo)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  listaArchivos
    }

    @SuppressLint("Range")
    public fun getArchivo(IdArchivo:BigInteger):Archivos_Model?{
        var archivo:Archivos_Model? = null

        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE?
        val columns:Array<String> =  arrayOf(
            DB_Info.tblArchivos.Col_IdArchivo,
            DB_Info.tblArchivos.Col_ArchivoData,
            DB_Info.tblArchivos.Col_TrabajoAsignado)

        val where:String =  "${DB_Info.tblArchivos.Col_IdArchivo} = ${IdArchivo.toString()}"

        val data =  dataBase.query(
            DB_Info.tblArchivos.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            DB_Info.tblArchivos.Col_IdArchivo + " ASC")

        if(data.moveToFirst()){
            archivo = Archivos_Model()
            //TODO: ESTO FUNCIONA CON "data.getString"
            archivo.IdArchivo = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_IdArchivo)).toBigInteger()
            archivo.ArchivoData =  data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_ArchivoData)) ?: ""
            archivo.TrabajoAsignado = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_TrabajoAsignado)).toBigInteger()


        }

        //dataBase.close()
        return archivo
    }
    
    @SuppressLint("Range")
    public fun getLowerIdArchivos():BigInteger?{
        var archivo:Archivos_Model? = null

        val columns:Array<String> =  arrayOf(
            DB_Info.tblArchivos.Col_IdArchivo)

        val where:String =  "${DB_Info.tblArchivos.Col_IdArchivo} <= -1"

        val data =  dataBase.query(
            DB_Info.tblArchivos.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            DB_Info.tblArchivos.Col_IdArchivo + " ASC LIMIT 1")

        var IdArchivoResult : BigInteger?

        if(data.moveToFirst()){
            IdArchivoResult = data.getString(data.getColumnIndex(DB_Info.tblArchivos.Col_IdArchivo)).toBigInteger()
            IdArchivoResult = IdArchivoResult!! - BigInteger("1")

        }
        else{
            IdArchivoResult = BigInteger("-1")
        }

        //dataBase.close()
        return IdArchivoResult
    }

    public fun updateArchivo(archivo:Archivos_Model):Boolean{

        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        val valuesArchivo: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesArchivo.put(DB_Info.tblArchivos.Col_ArchivoData,archivo.ArchivoData)
        valuesArchivo.put(DB_Info.tblArchivos.Col_TrabajoAsignado,archivo.TrabajoAsignado?.toLong()) //TODO: ESTO FUNCIONA?

        val where:String =  "${DB_Info.tblArchivos.Col_IdArchivo} = ?"

        //val where2:String = DB_Info.tblArchivos.COL_IDGENRE  + "=?" + "AND" + DB_Info.tblArchivos.COL_TITLE + "=?"
        //arrayOf(1,"HOLA")

        try{

            val result =  dataBase.update(
                DB_Info.tblArchivos.TABLE_NAME,
                valuesArchivo,
                where,
                arrayOf(archivo.IdArchivo.toString()))

            if (result != -1 ) {
                val strMessage = "Success"
            }
            else {
                val strMessage = "Failed"

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error editar  archivo SQLite", Toast.LENGTH_SHORT).show()
        }

        //dataBase.close()
        return  boolResult
    }

    fun deleteArchivo(IdArchivo:BigInteger):Boolean{

        var boolResult:Boolean =  false
        try{

            val where:String =  "${DB_Info.tblArchivos.Col_IdArchivo} = ?"
            val _success = dataBase.delete(
                DB_Info.tblArchivos.TABLE_NAME,
                where,
                arrayOf(IdArchivo.toString()))
            //dataBase.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error eliminar  archivo SQLite", Toast.LENGTH_SHORT).show()
        }

        return  boolResult
    }


    fun deleteArchivosTrabajo(IdTrabajo: BigInteger):Boolean{

        var boolResult:Boolean =  false
        try{

            val where:String =  "${DB_Info.tblArchivos.Col_TrabajoAsignado} = ?"
            val _success = dataBase.delete(
                DB_Info.tblArchivos.TABLE_NAME,
                where,
                arrayOf(IdTrabajo.toString()))
            //dataBase.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error eliminar  archivo SQLite", Toast.LENGTH_SHORT).show()
        }

        return  boolResult
    }


    fun limpiarArchivos(IdTrabajo:BigInteger):Boolean{

        var boolResult:Boolean =  false
        try{

            val where:String =  "${DB_Info.tblArchivos.Col_TrabajoAsignado} = ?"
            val _success = dataBase.delete(
                DB_Info.tblArchivos.TABLE_NAME,
                where,
                arrayOf(IdTrabajo.toString()))
            //dataBase.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error limpiar archivos SQLite", Toast.LENGTH_SHORT).show()
        }

        return  boolResult
    }


}
