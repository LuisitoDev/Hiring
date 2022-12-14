package com.psm.hiring.DAOs

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.CursorWindow
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import com.psm.hiring.General.DB_Info
import com.psm.hiring.Modelos.Usuarios_Model
import java.lang.reflect.Field
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList


class Usuarios_DAO (var context: Context, var dataBase:SQLiteDatabase){

    fun insertUsuario(usuario: Usuarios_Model):Boolean{

        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size


        val valuesUsuario: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesUsuario.put(DB_Info.tblUsuarios.Col_IdUsuario,usuario.IdUsuario?.toLong())
        valuesUsuario.put(DB_Info.tblUsuarios.Col_NombreUsuario,usuario.NombreUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario,usuario.ApellidoPaternoUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario,usuario.ApellidoMaternoUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_FechaNacimientoUsuario, usuario.FechaNacimientoUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_EscolaridadUsuario,usuario.EscolaridadUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_ProfesionUsuario,usuario.ProfesionUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_DescripcionUsuario,usuario.DescripcionUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_ImagenPerfilUsuario,usuario.ImagenPerfilUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_CorreoUsuario,usuario.CorreoUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_PasswordUsuario,usuario.PasswordUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_FechaCreacionUsuario,usuario.FechaCreacionUsuario)

        try {
            val result =  dataBase.insert(
                DB_Info.tblUsuarios.TABLE_NAME,
                null,
                valuesUsuario)

            if (result == (0).toLong()) {
                val strMessage = "Failed"
            }
            else {
                val strMessage = "Success"
            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error insertar Usuario SQLite", Toast.LENGTH_SHORT).show()
            boolResult =  false
        }

        //dataBase.close()

        return boolResult
    }

    @SuppressLint("Range")
    public fun getListaUsuarios():MutableList<Usuarios_Model>{
        val listaUsuarios:MutableList<Usuarios_Model> = ArrayList()

        
        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE? COMO POR EJEMPLO, PASSWORD?
        val columns:Array<String> =  arrayOf(DB_Info.tblUsuarios.Col_IdUsuario,
            DB_Info.tblUsuarios.Col_NombreUsuario,
            DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario,
            DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario,
            DB_Info.tblUsuarios.Col_FechaNacimientoUsuario,
            DB_Info.tblUsuarios.Col_EscolaridadUsuario,
            DB_Info.tblUsuarios.Col_ProfesionUsuario,
            DB_Info.tblUsuarios.Col_DescripcionUsuario,
            DB_Info.tblUsuarios.Col_ImagenPerfilUsuario,
            DB_Info.tblUsuarios.Col_CorreoUsuario,
            DB_Info.tblUsuarios.Col_PasswordUsuario,
            DB_Info.tblUsuarios.Col_FechaCreacionUsuario,
            DB_Info.tblUsuarios.Col_EstadoUsuario)

        val data =  dataBase.query(
            DB_Info.tblUsuarios.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            DB_Info.tblUsuarios.Col_IdUsuario + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                val usuario:Usuarios_Model = Usuarios_Model()
                //TODO: ESTO FUNCIONA CON "data.getString"
                usuario.IdUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_IdUsuario)).toBigInteger()
                usuario.NombreUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_NombreUsuario)) ?: ""
                usuario.ApellidoPaternoUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario)) ?: ""
                usuario.ApellidoMaternoUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario)) ?: ""
                usuario.FechaNacimientoUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_FechaNacimientoUsuario)) ?: ""
                usuario.EscolaridadUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_EscolaridadUsuario)) ?: ""
                usuario.ProfesionUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ProfesionUsuario)) ?: ""
                usuario.DescripcionUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_DescripcionUsuario)) ?: ""
                usuario.ImagenPerfilUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ImagenPerfilUsuario)) ?: ""
                usuario.CorreoUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_CorreoUsuario)) ?: ""
                usuario.PasswordUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_PasswordUsuario)) ?: ""
                usuario.FechaCreacionUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_FechaCreacionUsuario)) ?: ""
                usuario.EstadoUsuario = data.getInt(data.getColumnIndex(DB_Info.tblUsuarios.Col_EstadoUsuario)).toByte()

                listaUsuarios.add(usuario)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  listaUsuarios
    }

    @SuppressLint("Range")
    public fun getUsuario(IdUsuario:BigInteger):Usuarios_Model?{
        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        var usuario:Usuarios_Model? = null
        

        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE? COMO POR EJEMPLO, PASSWORD?
        val columns:Array<String> =  arrayOf(DB_Info.tblUsuarios.Col_IdUsuario,
            DB_Info.tblUsuarios.Col_NombreUsuario,
            DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario,
            DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario,
            DB_Info.tblUsuarios.Col_FechaNacimientoUsuario,
            DB_Info.tblUsuarios.Col_EscolaridadUsuario,
            DB_Info.tblUsuarios.Col_ProfesionUsuario,
            DB_Info.tblUsuarios.Col_DescripcionUsuario,
            DB_Info.tblUsuarios.Col_ImagenPerfilUsuario,
            DB_Info.tblUsuarios.Col_CorreoUsuario,
            DB_Info.tblUsuarios.Col_PasswordUsuario,
            DB_Info.tblUsuarios.Col_FechaCreacionUsuario,
            DB_Info.tblUsuarios.Col_EstadoUsuario)

        val where:String =  "${DB_Info.tblUsuarios.Col_IdUsuario} = ${IdUsuario.toString()}"

        val data =  dataBase.query(
            DB_Info.tblUsuarios.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            DB_Info.tblUsuarios.Col_IdUsuario + " ASC")

        if(data.moveToFirst()){
            usuario = Usuarios_Model()
            
            usuario.IdUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_IdUsuario)).toBigInteger()
            usuario.NombreUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_NombreUsuario)) ?: ""
            usuario.ApellidoPaternoUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario)) ?: ""
            usuario.ApellidoMaternoUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario)) ?: ""
            usuario.FechaNacimientoUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_FechaNacimientoUsuario)) ?: ""
            usuario.EscolaridadUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_EscolaridadUsuario)) ?: ""
            usuario.ProfesionUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ProfesionUsuario)) ?: ""
            usuario.DescripcionUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_DescripcionUsuario)) ?: ""
            usuario.ImagenPerfilUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ImagenPerfilUsuario)) ?: ""
            usuario.CorreoUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_CorreoUsuario)) ?: ""
            usuario.PasswordUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_PasswordUsuario)) ?: ""
            usuario.FechaCreacionUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_FechaCreacionUsuario)) ?: ""
            usuario.EstadoUsuario = data.getInt(data.getColumnIndex(DB_Info.tblUsuarios.Col_EstadoUsuario)).toByte()

        }

        //dataBase.close()
        return usuario
    }


    @SuppressLint("Range")
    public fun getUsuarioCreadorTrabajo(IdUsuario:BigInteger):Usuarios_Model?{
        var usuario:Usuarios_Model? = null
        

        //TODO: ES NECESARIO TRAER TODAS LAS COLUMNAS SIEMPRE? COMO POR EJEMPLO, PASSWORD?
        val columns:Array<String> =  arrayOf(DB_Info.tblUsuarios.Col_IdUsuario,
            DB_Info.tblUsuarios.Col_NombreUsuario,
            DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario,
            DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario,
            DB_Info.tblUsuarios.Col_ProfesionUsuario)

        val where:String =  "${DB_Info.tblUsuarios.Col_IdUsuario} = ${IdUsuario.toString()}"

        val data =  dataBase.query(
            DB_Info.tblUsuarios.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            DB_Info.tblUsuarios.Col_IdUsuario + " ASC")

        if(data.moveToFirst()){
            usuario = Usuarios_Model()
            //TODO: ESTO FUNCIONA CON "data.getString"
            usuario.IdUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_IdUsuario)).toBigInteger()
            usuario.NombreUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_NombreUsuario)) ?: ""
            usuario.ApellidoPaternoUsuario = data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario)) ?: ""
            usuario.ApellidoMaternoUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario)) ?: ""
            usuario.ProfesionUsuario =  data.getString(data.getColumnIndex(DB_Info.tblUsuarios.Col_ProfesionUsuario)) ?: ""
        }

        //dataBase.close()
        return usuario
    }

    public fun updateUsuario(usuario:Usuarios_Model):Boolean{

        val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.setAccessible(true)
        field.set(null, 100 * 1024 * 1024) //the 100MB is the new size

        val valuesUsuario: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        valuesUsuario.put(DB_Info.tblUsuarios.Col_IdUsuario,usuario.IdUsuario?.toLong())
        valuesUsuario.put(DB_Info.tblUsuarios.Col_NombreUsuario,usuario.NombreUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_ApellidoPaternoUsuario,usuario.ApellidoPaternoUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_ApellidoMaternoUsuario,usuario.ApellidoMaternoUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_FechaNacimientoUsuario, usuario.FechaNacimientoUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_EscolaridadUsuario,usuario.EscolaridadUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_ProfesionUsuario,usuario.ProfesionUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_DescripcionUsuario,usuario.DescripcionUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_ImagenPerfilUsuario,usuario.ImagenPerfilUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_CorreoUsuario,usuario.CorreoUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_PasswordUsuario,usuario.PasswordUsuario)
        valuesUsuario.put(DB_Info.tblUsuarios.Col_FechaCreacionUsuario,usuario.FechaCreacionUsuario)

        val where:String =  "${DB_Info.tblUsuarios.Col_IdUsuario} = ?"

        try{

            val result =  dataBase.update(
                DB_Info.tblUsuarios.TABLE_NAME,
                valuesUsuario,
                where,
                arrayOf(usuario.IdUsuario.toString()))

            if (result != -1 ) {
                val strMessage = "Success"
            }
            else {
                val strMessage = "Failed"

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error editar Usuario  SQLite ", Toast.LENGTH_SHORT).show()
        }

        return  boolResult
    }

    public fun deleteUsuario(IdUsuario:BigInteger):Boolean{
        
        var boolResult:Boolean =  false
        try{

            val where:String =  "${DB_Info.tblUsuarios.Col_IdUsuario} = ?"
            val _success = dataBase.delete(
                DB_Info.tblUsuarios.TABLE_NAME,
                where,
                arrayOf(IdUsuario.toString()))
            //dataBase.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
            Toast.makeText(this.context, "Error eliminar Usuario SQLite", Toast.LENGTH_SHORT).show()
        }

        return  boolResult
    }



}
