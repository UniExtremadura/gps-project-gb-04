package es.unex.giss.asee.ghiblitrunk.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.unex.giss.asee.ghiblitrunk.data.models.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class GhibliTrunkDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: GhibliTrunkDatabase? = null

        fun getInstance(context: Context): GhibliTrunkDatabase {
            synchronized(GhibliTrunkDatabase::class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GhibliTrunkDatabase::class.java, "ghiblitrunkDatabase.db"
                    ).build()
                }
                return INSTANCE as GhibliTrunkDatabase
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
