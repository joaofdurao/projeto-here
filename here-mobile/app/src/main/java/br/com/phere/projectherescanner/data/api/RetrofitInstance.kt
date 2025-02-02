package br.com.phere.projectherescanner.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://34.74.179.238:8080"
    private var authToken: String? = null

    fun setAuthToken(token: String) {
        authToken = token
    }

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request().newBuilder().apply {
            authToken?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()
        chain.proceed(request)
    }.build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}