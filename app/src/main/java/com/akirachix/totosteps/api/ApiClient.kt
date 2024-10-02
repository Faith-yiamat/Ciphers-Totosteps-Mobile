package com.akirachix.totosteps.api

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${getAuthToken()}")
            .build()
        return chain.proceed(request)
    }

    private fun getAuthToken(): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("AUTH_TOKEN", "") ?: ""
    }
}

object ApiClient {
    private const val BASE_URL = "https://totosteps-29a482165136.herokuapp.com"

    private var appContext: Context? = null

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    val instance: ApiInterface by lazy {
        val context = appContext ?: throw UninitializedPropertyAccessException("ApiClient must be initialized with a Context before use")

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiInterface::class.java)
    }
}





