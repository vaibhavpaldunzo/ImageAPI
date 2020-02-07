package com.example.imageapi

import android.app.Application

class MyApplication : Application() {
    companion object{
        var myApplicationContext : Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        myApplicationContext = this
    }
}