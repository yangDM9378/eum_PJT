package com.example.ieum.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object RetrofitImpl {
    private const val URL = "http://i-eum-u.com/api/v1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val service: RetrofitService = retrofit.create(RetrofitService::class.java)
}