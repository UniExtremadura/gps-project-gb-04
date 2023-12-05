package es.unex.giss.asee.ghiblitrunk.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.unex.giss.asee.ghiblitrunk.data.models.Character

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(person: Character)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<Character>)

    @Query("SELECT count(*) FROM characters")
    suspend fun getNumberOfCharacters(): Long

    @Query("SELECT * FROM characters")
    fun getAllPeople(): LiveData<List<Character>>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getPersonById(id: String): Character
}