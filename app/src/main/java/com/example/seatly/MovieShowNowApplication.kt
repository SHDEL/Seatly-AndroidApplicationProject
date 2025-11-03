package com.example.seatly

import android.app.Application
import com.example.seatly.data.AppContainer
import com.example.seatly.data.DefaultAppContainer

class MovieShowNowApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}