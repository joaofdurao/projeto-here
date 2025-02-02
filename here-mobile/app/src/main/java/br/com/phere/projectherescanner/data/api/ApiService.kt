package br.com.phere.projectherescanner.data.api

import br.com.phere.projectherescanner.data.auth.LoginRequest
import br.com.phere.projectherescanner.data.models.Evento
import okhttp3.ResponseBody
import retrofit2.http.*
import retrofit2.Response

interface ApiService {

    @POST("/usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<ResponseBody>

    @POST("/eventos")
    suspend fun createEvento(@Body evento: Evento): Response<Evento>

    @GET("/eventos")
    suspend fun getEventosByStatus(@Query("status") status: Boolean): List<Evento>

    @GET("eventos/{id}")
    suspend fun getEventoById(@Path("id") id: String): Evento

    @PUT("participacoes/confirmar-presenca/{id}")
    suspend fun confirmPresenca(@Path("id") id: String): Response<ResponseBody>


}