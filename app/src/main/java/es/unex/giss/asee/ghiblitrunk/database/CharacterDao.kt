package es.unex.giss.asee.ghiblitrunk.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.giss.asee.ghiblitrunk.data.models.Character

@Dao
interface CharacterDao {
    @Insert
    suspend fun insert(person: Character)

    @Query("SELECT * FROM characters")
    suspend fun getAllPeople(): List<Character>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getPersonById(id: String): Character
}