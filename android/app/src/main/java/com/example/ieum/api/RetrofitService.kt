package com.example.ieum.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface RetrofitService {


    @GET("pins")
    fun getPinAll(@Header("Authorization")token:String): Call<List<Result.Pin>>


    @GET("groups/{group_id}")
    fun getGroupAll(@Header("Authorization")token:String, @Path("group_id") id:Int): Call<Result.Response>
}