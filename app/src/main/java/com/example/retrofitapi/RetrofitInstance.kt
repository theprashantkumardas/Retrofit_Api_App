package com.example.retrofitapi



import com.example.retrofitapi.data.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Logs request and response body
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add the logging interceptor
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Api.BASE_URL) // Set base URL
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON parsing
            .client(httpClient) // Attach OkHttp client
            .build()
    }

    val api: Api by lazy {
        retrofit.create(Api::class.java) // Create API implementation
    }
}
