package com.ieumpyo.ieum.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="pin")
data class notifiedLocationEntity(
    @PrimaryKey
    val pin_id:Int
)
