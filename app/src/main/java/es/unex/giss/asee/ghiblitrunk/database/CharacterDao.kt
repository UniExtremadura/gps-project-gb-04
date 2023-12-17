package es.unex.giss.asee.ghiblitrunk.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun delete(character: Character)

    @Delete
    suspend fun delete(userCharacterCrossRef: UserCharacterCrossRef)

    @Query("SELECT is_favourite FROM characters Where id = :id")
    suspend fun getIfFavorite(id: String): Boolean

    @Query("SELECT * FROM characters Where is_favourite = 1")
    suspend fun getFavorites(): List<Character>

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



    // region Barra de búsqueda
    @Query("SELECT * FROM characters WHERE name LIKE :name")
    fun searchCharactersByName(name: String): LiveData<List<Character>>

    @Query("SELECT * FROM characters WHERE age LIKE :age")
    fun searchCharactersByAge(age: String): LiveData<List<Character>>

    @Query("SELECT * FROM characters WHERE gender LIKE :gender")
    fun searchCharactersByGender(gender: String): LiveData<List<Character>>

    // endregion
}