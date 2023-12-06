package es.unex.giss.asee.ghiblitrunk.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import es.unex.giss.asee.ghiblitrunk.data.models.UserCharacterCrossRef
import es.unex.giss.asee.ghiblitrunk.data.models.UserMovieCrossRef
import es.unex.giss.asee.ghiblitrunk.data.models.UserWithCharacters
import es.unex.giss.asee.ghiblitrunk.data.models.UserWithMovies

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(person: Character)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<Character>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserCharacter(crossRef: UserCharacterCrossRef)

    @Query("SELECT count(*) FROM characters")
    suspend fun getNumberOfCharacters(): Long

    @Query("SELECT * FROM characters")
    fun getAllPeople(): LiveData<List<Character>>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getPersonById(id: String): Character

    @Update
    suspend fun update(character: Character)

    @Transaction
    @Query("SELECT * FROM User where userId = :userId")
    fun getUserWithCharacters(userId: Long): LiveData<UserWithCharacters>
}