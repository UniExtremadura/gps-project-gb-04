package es.unex.giss.asee.ghiblitrunk

import es.unex.giss.asee.ghiblitrunk.api.ApiService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiTest {

    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        // Configurar Retrofit y crear la instancia de ApiService
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ghibliapi.vercel.app/") // Reemplaza con tu URL de prueba
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @Test
    fun testAllPeople() = runBlocking {
            val result = apiService.getAllPeople()
            // Aquí puedes realizar las aserciones según tus expectativas
            assert(result.isSuccessful)
            assert(result.body() != null)
    }

    @Test
    fun testGetAllFilms() = runBlocking {
        val result = apiService.getAllFilms()
        // Aquí puedes realizar las aserciones según tus expectativas
        assert(result.isSuccessful)
        assert(result.body() != null)
    }


}