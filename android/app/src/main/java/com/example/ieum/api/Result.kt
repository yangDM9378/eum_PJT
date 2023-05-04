package com.example.ieum.api

import java.sql.Date
import java.text.DateFormat


public class Result {

    data class Pin(
        val pin_id : Int,
        val latitude : Double,
        val longitude : Double,
        val radius: Float = 1000f
    )
    data class Group(
        val name: String,
        val createdDate: Any,
        val description: String,
        val image: String
    )
    data class ResponsePin(
        val result: List<Pin>,
        val resultCode: String,
        val resultMsg: String
    )
    data class ResponseGroup(
        val result: Group,
        val resultCode:String,
        val resultMsg: String
    )
    
}


