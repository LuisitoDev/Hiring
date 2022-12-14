package com.psm.hiring.Modelos

import java.math.BigInteger


class Trabajos_Model(){
    var IdTrabajo: BigInteger? = null;
    var TituloTrabajo:String? = null;
    var DescripcionTrabajo:String? = null;
    var PagoTrabajo: String? = null;

    var StatusTrabajo:String? = null;
    var FechaCreacionTrabajo:String? = null;
    var EstadoTrabajo:Byte? = null;
    var UsuarioCreador: BigInteger? = null;

    var ListaArchivosModel:MutableList<Archivos_Model>? = arrayListOf();
    var UsuarioCreadorModel:Usuarios_Model? = Usuarios_Model();
    var SolicitudAplicadaModel:Solicitudes_Model? = Solicitudes_Model();

    var UsuarioSolicita: BigInteger? = null;

    var NumeroTrabajoPaginacion: Int? = null;
    var PagoTrabajoDesde:String? = null;
    var PagoTrabajoHasta:String? = null;
    var FechaDesdeCreacionTrabajo:String? = null;
    var FechaHastaCreacionTrabajo:String? = null;
    var StatusSolicitud:String? = null;
    var FiltroBusqueda:String? = null;


    private constructor(IdTrabajo: BigInteger?, TituloTrabajo:String?, DescripcionTrabajo:String?, PagoTrabajo:String?,
                StatusTrabajo:String?, FechaCreacionTrabajo:String?, EstadoTrabajo:Byte?, UsuarioCreador: BigInteger?,
                ListaArchivosModel:MutableList<Archivos_Model>?, UsuarioCreadorModel:Usuarios_Model?, SolicitudAplicadaModel:Solicitudes_Model?, UsuarioSolicita:BigInteger?,
                NumeroTrabajoPaginacion:Int?, PagoTrabajoDesde: String?, PagoTrabajoHasta: String?,
                FechaDesdeCreacionTrabajo: String?, FechaHastaCreacionTrabajo: String?, StatusSolicitud:String?, FiltroBusqueda:String?

    ) :this(){
        this.IdTrabajo = IdTrabajo
        this.TituloTrabajo = TituloTrabajo
        this.DescripcionTrabajo = DescripcionTrabajo
        this.PagoTrabajo = PagoTrabajo
        this.StatusTrabajo = StatusTrabajo
        this.FechaCreacionTrabajo = FechaCreacionTrabajo
        this.EstadoTrabajo = EstadoTrabajo
        this.UsuarioCreador = UsuarioCreador

        this.ListaArchivosModel = ListaArchivosModel
        this.UsuarioCreadorModel = UsuarioCreadorModel
        this.SolicitudAplicadaModel = SolicitudAplicadaModel

        this.UsuarioSolicita = UsuarioSolicita
        this.NumeroTrabajoPaginacion = NumeroTrabajoPaginacion
        this.PagoTrabajoDesde = PagoTrabajoDesde
        this.PagoTrabajoHasta = PagoTrabajoHasta
        this.FechaDesdeCreacionTrabajo = FechaDesdeCreacionTrabajo
        this.FechaHastaCreacionTrabajo = FechaHastaCreacionTrabajo
        this.StatusSolicitud = StatusSolicitud
        this.FiltroBusqueda = FiltroBusqueda

    }


    data class Builder(
        var IdTrabajo: BigInteger? = null,
        var TituloTrabajo:String? = null,
        var DescripcionTrabajo:String? = null,
        var PagoTrabajo: String? = null,

        var StatusTrabajo:String? = null,
        var FechaCreacionTrabajo:String? = null,
        var EstadoTrabajo:Byte? = null,
        var UsuarioCreador: BigInteger? = null,

        var ListaArchivosModel:MutableList<Archivos_Model>? = null,
        var UsuarioCreadorModel:Usuarios_Model? = null,
        var SolicitudAplicadaModel:Solicitudes_Model? = null,

        var UsuarioSolicita: BigInteger? = null,
        var NumeroTrabajoPaginacion: Int? = null,
        var PagoTrabajoDesde:String? = null, //TODO: String?
        var PagoTrabajoHasta:String? = null, //TODO: String?
        var FechaDesdeCreacionTrabajo:String? = null, //TODO: String?
        var FechaHastaCreacionTrabajo:String? = null, //TODO: String?
        var StatusSolicitud:String? = null,
        var FiltroBusqueda:String? = null) {



        fun setIdTrabajo(IdTrabajo: BigInteger) = apply { this.IdTrabajo = IdTrabajo }
        fun setTituloTrabajo(TituloTrabajo: String) = apply { this.TituloTrabajo = TituloTrabajo }
        fun setDescripcionTrabajo(DescripcionTrabajo: String) = apply { this.DescripcionTrabajo = DescripcionTrabajo }
        fun setPagoTrabajo(PagoTrabajo: String) = apply { this.PagoTrabajo = PagoTrabajo }
        fun setStatusTrabajo(StatusTrabajo: String) = apply { this.StatusTrabajo = StatusTrabajo }
        fun setFechaCreacionTrabajo(FechaCreacionTrabajo: String) = apply { this.FechaCreacionTrabajo = FechaCreacionTrabajo }
        fun setEstadoTrabajo(EstadoTrabajo: Byte) = apply { this.EstadoTrabajo = EstadoTrabajo }
        fun setUsuarioCreador(UsuarioCreador: BigInteger) = apply { this.UsuarioCreador = UsuarioCreador }

        fun setListaArchivosModel(ListaArchivosModel: MutableList<Archivos_Model>) = apply { this.ListaArchivosModel = ListaArchivosModel }
        fun setUsuarioCreadorModel(UsuarioCreadorModel: Usuarios_Model) = apply { this.UsuarioCreadorModel = UsuarioCreadorModel }
        fun setSolicitudAplicadaModel(SolicitudAplicadaModel: Solicitudes_Model) = apply { this.SolicitudAplicadaModel = SolicitudAplicadaModel }


        fun setUsuarioSolicita(UsuarioSolicita: BigInteger) = apply { this.UsuarioSolicita = UsuarioSolicita }
        fun setNumeroTrabajoPaginacion(NumeroTrabajoPaginacion: Int) = apply { this.NumeroTrabajoPaginacion = NumeroTrabajoPaginacion }
        fun setPagoTrabajoDesde(PagoTrabajoDesde: String) = apply { this.PagoTrabajoDesde = PagoTrabajoDesde }
        fun setPagoTrabajoHasta(PagoTrabajoHasta: String) = apply { this.PagoTrabajoHasta = PagoTrabajoHasta }
        fun setFechaDesdeCreacionTrabajo(FechaDesdeCreacionTrabajo: String) = apply { this.FechaDesdeCreacionTrabajo = FechaDesdeCreacionTrabajo }
        fun setFechaHastaCreacionTrabajo(FechaHastaCreacionTrabajo: String) = apply { this.FechaHastaCreacionTrabajo = FechaHastaCreacionTrabajo }
        fun setStatusSolicitud(StatusSolicitud: String) = apply { this.StatusSolicitud = StatusSolicitud }
        fun setFiltroBusqueda(FiltroBusqueda: String) = apply { this.FiltroBusqueda = FiltroBusqueda }


        fun build() = Trabajos_Model(IdTrabajo, TituloTrabajo, DescripcionTrabajo, PagoTrabajo,
                                    StatusTrabajo, FechaCreacionTrabajo, EstadoTrabajo, UsuarioCreador,
                                    ListaArchivosModel, UsuarioCreadorModel, SolicitudAplicadaModel, UsuarioSolicita, NumeroTrabajoPaginacion,
                                    PagoTrabajoDesde, PagoTrabajoHasta, FechaDesdeCreacionTrabajo, FechaHastaCreacionTrabajo, StatusSolicitud, FiltroBusqueda)
    }
}