package com.example.ieum.api


public class Result {
    data class Pin(
        val pin_id : Int,
        val latitude : Double,
        val longitude : Double,
        val radius: Float = 1000f
    )
    data class Response(
        val code: Int,
        val message: String,
        val status: Int
    )
    
}


