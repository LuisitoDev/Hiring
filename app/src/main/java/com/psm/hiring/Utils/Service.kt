package com.psm.hiring.Utils

import com.psm.hiring.Modelos.*
import retrofit2.Call
import retrofit2.http.*
import java.math.BigInteger

//Retrofi usa una interface para hacer la petición hacia el servidor
interface Service{
    /*
    @GET("Album/Albums")
    fun getAlbums():Call<List<Album>>


    @GET("Album/Albums/{id}")
    fun getAlbum(@Path("id") id: Int): Call<List<Album>>
    */

    //TODO: VER COMO RECIBIR LAS RESPUESTAS DE JSON CUANDO NO RETORNAMOS UN OBJETO


    @GET("cuenta/perfil/{IdUsuario}")
    fun getUsuario(@Path("IdUsuario") IdUsuario: BigInteger): Call<Usuarios_Model>

    @Headers("Content-Type: application/json")
    @POST("cuenta/login")
    fun loginUsuario(@Body usuario: Usuarios_Model):Call<Usuarios_Model>
    //CorreoUsuario:String, PasswordUsuario:String


    @GET("cReadUsers.php")
    fun leerUsuarios():Call<List<Usuarios_Model>>

    @GET("cReadUsers.php?IdUsuario={IdUsuario}")
    fun leerUsuariosPorId(@Path("IdUsuario") IdUsuario: BigInteger): Call<Usuarios_Model>
    //Nuestro endpoint retornaría:
    //NombreUsuario:String, ApellidoPaternoUsuario:String, ApellidoMaternoUsuario:String
    //EscolaridadUsuario:String, FechaNacimientoUsuario:String, ProfesionUsuario:String
    //DescripcionUsuario:String, ImagenPerfilUsuario:String, CorreoUsuario:String, PasswordUsuario:String

    @Headers("Content-Type: application/json")
    @POST("cuenta/editar-usuario")
    fun editarUsuario(@Body usuario: Usuarios_Model):Call<RequestResponseAPI>
    //IdUsuario
    //NombreUsuario:String, ApellidoPaternoUsuario:String, ApellidoMaternoUsuario:String
    //EscolaridadUsuario:String, FechaNacimientoUsuario:String, ProfesionUsuario:String
    //DescripcionUsuario:String, ImagenPerfilUsuario:String, CorreoUsuario:String, PasswordUsuario:String

    @Headers("Content-Type: application/json")
    @POST("cuenta/actualizar-password")
    fun editarPassword(@Body usuario: Usuarios_Model):Call<RequestResponseAPI>
    //IdUsuario
    //Password:String

    @Headers("Content-Type: application/json")
    @POST("cuenta/buscar-correo")
    fun checarCorreo(@Body usuario: Usuarios_Model):Call<Usuarios_Model>
    //CorreoUsuario:String, PasswordUsuario:String






    @GET("trabajos/inicio/{NumeroTrabajoPaginacion}")
    fun getTrabajosInicio(@Path("NumeroTrabajoPaginacion") NumeroTrabajoPaginacion: Int): Call<List<Trabajos_Model>>


    @GET("trabajos/detalles/{IdTrabajo}")
    fun getTrabajo(@Path("IdTrabajo") IdTrabajo: BigInteger): Call<Trabajos_Model>


    @Headers("Content-Type: application/json")
    @POST("trabajos/mis-trabajos-creados")
    fun getMisTrabajos(@Body Trabajo: Trabajos_Model):Call<List<Trabajos_Model>>
    //UsuarioCreador: BigInteger, StatusTrabajo: String, NumeroTrabajoPaginacion: Int

    @Headers("Content-Type: application/json")
    @POST("trabajos/mis-solicitudes-trabajos")
    fun getMisSolicitudesTrabajo(@Body Trabajo: Trabajos_Model):Call<List<Trabajos_Model>>
    //UsuarioSolicita: BigInteger, StatusTrabajo: String, NumeroTrabajoPaginacion: Int


    @Headers("Content-Type: application/json")
    @POST("trabajos/busqueda-avanzada")
    fun getBusquedaAvanzadaTrabajos(@Body Trabajo: Trabajos_Model):Call<List<Trabajos_Model>>
    //TituloTrabajo: String, PagoTrabajoDesde: String, PagoTrabajoHasta: String
    //FechaDesdeCreacionTrabajo: String, FechaHastaCreacionTrabajo: String, NumeroTrabajoPaginacion: Int


    @Headers("Content-Type: application/json")
    @POST("trabajos/crear-trabajo")
    fun crearTrabajo(@Body Trabajo: Trabajos_Model):Call<RequestResponseAPI>
    //TituloTrabajo:String, DescripcionTrabajo:String, PagoTrabajo:String, SatusTrabajo:String
    //UsuarioCreador:BigInterger, archivosTrabajo[]: String[] (TODO: CÓMO FUNCIONARA ESTO?)

    @Headers("Content-Type: application/json")
    @POST("trabajos/editar-trabajo")
    fun editarTrabajo(@Body Trabajo: Trabajos_Model):Call<RequestResponseAPI>
    //IdTrabajo: String
    //TituloTrabajo:String, DescripcionTrabajo:String, PagoTrabajo:String, SatusTrabajo:String
    //UsuarioCreador:BigInterger (TODO: ES NECESARIO?), archivosTrabajo[]: String[] (TODO: CÓMO FUNCIONARA ESTO?)


    @Headers("Content-Type: application/json")
    @POST("trabajos/delete-trabajo")
    fun deleteTrabajos(@Body Trabajo: Trabajos_Model):Call<RequestResponseAPI>
    //IdTrabajo: String


    @Headers("Content-Type: application/json")
    @POST("trabajos/cerrar-trabajo")
    fun closeTrabajos(@Body Trabajo: Trabajos_Model):Call<RequestResponseAPI>
    //IdTrabajo: String














    @Headers("Content-Type: application/json")
    @POST("solicitudes/solicitud")
    fun getSolicitud(@Body solicitud: Solicitudes_Model):Call<Solicitudes_Model>
    //UsuarioSolicita:BigInterger, TrabajoSolicitado:BigInterger



    @Headers("Content-Type: application/json")
    @POST("solicitudes/cargar-solicitudes-notificaciones")
    fun getSolicitudesNotificaciones(@Body solicitud: Solicitudes_Model):Call<List<Solicitudes_Model>>
    //UsuarioSolicita:BigInterger


    @Headers("Content-Type: application/json")
    @POST("solicitudes/cargar-solicitudes-trabajo")
    fun getSolicitudesMiTrabajo(@Body solicitud: Solicitudes_Model):Call<List<Solicitudes_Model>>
    //TrabajoSolicitado:BigInterger, StatusSolicitud::String


    @Headers("Content-Type: application/json")
    @POST("solicitudes/crear-solicitud")
    fun crearSolicitud(@Body solicitud: Solicitudes_Model):Call<RequestResponseAPI>
    //UsuarioSolicita:BigInterger, TrabajoSolicitado:BigInterger, StatusSolicitud:String


    @Headers("Content-Type: application/json")
    @POST("solicitudes/editar-solicitud")
    fun editarSolicitud(@Body solicitud: Solicitudes_Model):Call<RequestResponseAPI>
    //UsuarioSolicita:BigInterger, TrabajoSolicitado:BigInterger
    // StatusSolicitud:String, FechaRespuestaSolicitud:String


    @Headers("Content-Type: application/json")
    @POST("solicitudes/delete-solicitud")
    fun deleteSolicitud(@Body solicitud: Solicitudes_Model):Call<RequestResponseAPI>
    //UsuarioSolicita:BigInterger, TrabajoSolicitado:BigInterger






    @Headers("Content-Type: application/json")
    @POST("mensajes/crear-mensaje")
    fun crearMensaje(@Body mensaje: Mensajes_Model):Call<RequestResponseAPI>
    //UsuarioEnvia:BigInterger, UsuarioRecibe:BigInterger, DescripcionMensaje:String


    @Headers("Content-Type: application/json")
    @POST("mensajes/mis-conversaciones")
    fun getConversaciones(@Body mensaje: Mensajes_Model):Call<List<Mensajes_Model>>
    //UsuarioEnvia:BigInterger



    @Headers("Content-Type: application/json")
    @POST("mensajes/filtro-conversaciones")
    fun getConversacionesFiltro(@Body mensagje: Mensajes_Model):Call<List<Mensajes_Model>>
    //UsuarioEnvia:BigInterger, FiltroBandeja:String (TODO: FALTA AGREGARLO A LOS Mensajes_Model)



    @Headers("Content-Type: application/json")
    @POST("mensajes/mis-mensajes-conversacion")
    fun getMensajesConversacion(@Body mensaje: Mensajes_Model):Call<List<Mensajes_Model>>
    //UsuarioEnvia:BigInterger, UsuarioRecibe:BigInterger



}