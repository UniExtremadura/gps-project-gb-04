package es.unex.giss.asee.ghiblitrunk.data.models
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: String = "",
    var title: String = "",
    var original_title: String = "",
    val original_title_romanised: String = "",
    var description: String = "",
    var director: String = "",
    var producer: String = "",
    var release_date: String = "",
    val running_time: String = "",
    val rt_score: String = "",
    @TypeConverters(Converters::class) val people: List<String> = emptyList(),
    @TypeConverters(Converters::class) val species: List<String> = emptyList(),
    @TypeConverters(Converters::class) val locations: List<String> = emptyList(),
    @TypeConverters(Converters::class) val vehicles: List<String> = emptyList(),
    val url: String = "",
    @ColumnInfo(name="is_favourite") var isFavourite: Boolean = false
): Serializable

