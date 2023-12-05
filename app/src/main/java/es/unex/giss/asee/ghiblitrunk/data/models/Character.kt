package es.unex.giss.asee.ghiblitrunk.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "characters")
data class Character(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val gender: String = "",
    val age: String = "",
    val eye_color: String = "",
    val hair_color: String = "",
    @TypeConverters(Converters::class) val films: List<String> = emptyList(),
    val species: String = "",
    val url: String = "",
    @ColumnInfo(name="is_favourite") var isFavourite: Boolean = false
)
