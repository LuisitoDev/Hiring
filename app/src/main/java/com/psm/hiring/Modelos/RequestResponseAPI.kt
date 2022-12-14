package com.psm.hiring.Modelos

import java.math.BigInteger

class RequestResponseAPI() {
    var status: String? = null;
    var IdUsuario: BigInteger? = null;
    var IdTrabajo: BigInteger? = null;

    constructor(status: String?, IdUsuario:BigInteger?, IdTrabajo: BigInteger?) :this(){
        this.status = status
        this.IdUsuario = IdUsuario
        this.IdTrabajo = IdTrabajo
    }

}