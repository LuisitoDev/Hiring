package com.psm.hiring.Database

import android.app.Application
import android.content.Context
import com.psm.hiring.DAOs.*

class UserApplication()  {


    companion object{

        lateinit var dbHelper: Hiring_DB
    }

    fun initialize(applicationContext : Context) {
        dbHelper =  Hiring_DB(applicationContext)
    }


}