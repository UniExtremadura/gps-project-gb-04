package es.unex.giss.asee.ghiblitrunk.api

import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Call

interface ApiService {

    // Endpoint para obtener todas las películas
    @GET("films")
    fun getAllFilms(): Call<List<Movie>>

    // Endpoint para obtener una película por ID
    @GET("films/{id}")
    fun getFilmById(@Path("id") id: String): Call<Movie>

    // Endpoint para obtener todas las personas
    @GET("people")
    fun getAllPeople(): Call<List<Character>>

    // Endpoint para obtener una persona por ID
    @GET("people/{id}")
    fun getPersonById(@Path("id") id: String): Call<Character>
}