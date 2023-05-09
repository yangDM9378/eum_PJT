package com.ieumpyo.ieum.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [notifiedLocationEntity::class],version=2)
abstract class notifiedLocationDB :RoomDatabase(){
    abstract fun dao() : notifiedLocationDAO
}