package com.rmiragaya.deptnasachallenge.api

import com.rmiragaya.deptnasachallenge.utils.Constants.Companion.BASE_URL
import com.rmiragaya.deptnasachallenge.utils.Constants.Companion.READ_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .connectTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val nasaApi by lazy {
            retrofit.create(NasaApi::class.java)
        }
    }
}