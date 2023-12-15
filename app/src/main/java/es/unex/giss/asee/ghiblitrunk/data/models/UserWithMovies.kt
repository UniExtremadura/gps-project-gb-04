package es.unex.giss.asee.ghiblitrunk.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
data class UserWithMovies(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id",
        associateBy = Junction(
            value = UserMovieCrossRef::class,
            parentColumn = "userId",
            entityColumn = "movieId"
        )
    )
    val movies: List<Movie>
)