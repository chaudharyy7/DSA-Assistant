package com.example.codenation.editor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: JdoodleApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jdoodle.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JdoodleApiService::class.java)
    }
}
