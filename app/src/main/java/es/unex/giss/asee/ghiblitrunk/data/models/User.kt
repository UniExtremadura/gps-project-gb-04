package es.unex.giss.asee.ghiblitrunk.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Long?,
    var name: String = "",
    var password: String = ""
): Serializable