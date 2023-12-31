package es.unex.giss.asee.ghiblitrunk.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "characters")
data class Character(
    @PrimaryKey val id: String = "",
    var name: String = "",
    var gender: String = "",
    var age: String = "",
    var eye_color: String = "",
    var hair_color: String = "",
    @TypeConverters(Converters::class) val films: List<String> = emptyList(),
    val species: String = "",
    val url: String = "",
    @ColumnInfo(name="is_favourite") var isFavourite: Boolean = false
): Serializable
