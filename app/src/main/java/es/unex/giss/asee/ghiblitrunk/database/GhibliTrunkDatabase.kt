package es.unex.giss.asee.ghiblitrunk.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Comment
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.data.models.User
import es.unex.giss.asee.ghiblitrunk.data.models.Converters
import es.unex.giss.asee.ghiblitrunk.data.models.UserCharacterCrossRef
import es.unex.giss.asee.ghiblitrunk.data.models.UserMovieCrossRef

@Database(entities = [User::class, Movie::class, Character::class, Comment::class, UserMovieCrossRef::class, UserCharacterCrossRef::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GhibliTrunkDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
    abstract fun characterDao(): CharacterDao
    abstract fun commentDao(): CommentDao

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
