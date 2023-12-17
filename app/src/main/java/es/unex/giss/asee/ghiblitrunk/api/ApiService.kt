package es.unex.giss.asee.ghiblitrunk.api

import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.data.models.Movie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Response

interface ApiService {

    // Endpoint para obtener todas las películas
    @GET("films")
    suspend fun getAllFilms(): Response<List<Movie>>

    // Endpoint para obtener una película por ID
    @GET("films/{id}")
    suspend fun getFilmById(@Path("id") id: String): Response<Movie>

    // Endpoint para obtener todas las personas
    @GET("people")
    suspend fun getAllPeople(): Response<List<Character>>

    // Endpoint para obtener una persona por ID
    @GET("people/{id}")
    suspend fun getPersonById(@Path("id") id: String): Response<Character>
}