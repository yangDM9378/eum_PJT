package com.example.ieum.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {


    @GET("pins")
    fun getPinAll(): Call<List<Result.Pin>>


    @GET("groups/{group_id}")
    fun getGroupAll(@Path("group_id") id:Int): Call<Result>
}