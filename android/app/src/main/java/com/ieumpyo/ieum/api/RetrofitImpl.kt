package com.ieumpyo.ieum.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitImpl {
//    private const val URL = "http://10.0.2.2:8080/"
    private const val URL = "https://i-eum-u.com/api/v1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val service: RetrofitService = retrofit.create(RetrofitService::class.java)
}