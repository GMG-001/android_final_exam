package com.example.finalexam.room

import androidx.room.*

@Dao
interface Methods {
    @Query("SELECT * FROM alarm")
    fun getAllInfo(): List<Info>?

    @Query("SELECT * FROM alarm C WHERE C.ID IN (:Ids)")
    fun getInfoByIds(Ids: IntArray): List<Info>

    @Query("SELECT * FROM alarm C ORDER BY C.is_active DESC")
    fun getAlarmsSortedByActiveDesc(): List<Info>

    @Query("UPDATE alarm SET is_active=:isActive WHERE id=:id")
    fun updateAlarmStatus(id: Long, isActive: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg info: Info)

    @Delete
    fun deleteAlarm(info: Info)

    @Query("DELETE FROM alarm")
    fun deleteAll()
}