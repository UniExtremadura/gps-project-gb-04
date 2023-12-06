package es.unex.giss.asee.ghiblitrunk

import android.app.Application
import es.unex.giss.asee.ghiblitrunk.utils.AppContainer

class GhibliTrunkApplication: Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }

}