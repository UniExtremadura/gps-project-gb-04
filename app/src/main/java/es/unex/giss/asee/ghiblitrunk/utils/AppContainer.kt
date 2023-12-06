package es.unex.giss.asee.ghiblitrunk.utils

import android.content.Context
import es.unex.giss.asee.ghiblitrunk.api.RetrofitClient
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase

class AppContainer(context: Context) {

    private val networkService = RetrofitClient.apiService
    private val db = GhibliTrunkDatabase.getInstance(context!!)
    val repository = Repository(db.characterDao(), db.movieDao(), networkService)
}