package com.ieumpyo.ieum.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface notifiedLocationDAO {
    @Query("SELECT pin_id FROM pin ORDER BY pin_id asc")
    fun getAll(): LiveData<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notifiedPinEntity: notifiedLocationEntity)
}