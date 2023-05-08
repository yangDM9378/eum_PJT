package com.ieumpyo.ieum.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="pin")
data class notifiedLocationEntity(
    val pin_id:Int
){
    @PrimaryKey(autoGenerate = true)
    var notified_id:Int=0
}
