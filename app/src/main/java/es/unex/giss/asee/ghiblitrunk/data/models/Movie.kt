package es.unex.giss.asee.ghiblitrunk.data.models
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: String,
    val title: String,
    val original_title: String,
    val original_title_romanised: String,
    val description: String,
    val director: String,
    val producer: String,
    val release_date: String,
    val running_time: String,
    val rt_score: String,
    @TypeConverters(Converters::class) val people: List<String>,
    @TypeConverters(Converters::class) val species: List<String>,
    @TypeConverters(Converters::class) val locations: List<String>,
    @TypeConverters(Converters::class) val vehicles: List<String>,
    val url: String,
    @ColumnInfo(name="is_favourite") var isFavourite: Boolean = false
)

