package es.unex.giss.asee.ghiblitrunk

import android.content.Context
import es.unex.giss.asee.ghiblitrunk.data.Repository
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.api.RetrofitClient

class TestRepository(context: Context) : Repository(
    GhibliTrunkDatabase.getInstance(context).characterDao(),
    GhibliTrunkDatabase.getInstance(context).movieDao(),
    GhibliTrunkDatabase.getInstance(context).userDao(),
    GhibliTrunkDatabase.getInstance(context).commentDao(),
    RetrofitClient.apiService
) {

}