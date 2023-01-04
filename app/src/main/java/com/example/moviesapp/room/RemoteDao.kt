package com.example.moviesapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.model.RemoteKeys

@Dao
interface RemoteDao {

    //To read all data in Keys data base :
    @Query("SELECT * FROM keys_table WHERE movie_id = :id")
    suspend fun readRemoteKeys(id: Int) : RemoteKeys?

    //To add remote keys :
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRemoteKeys(remoteKeys:List<RemoteKeys?>)

    //To delete all remote keys:
    @Query("DELETE FROM keys_table")
    suspend fun deleteAllRemoteKeys()

    @Query("Select created_at From keys_table Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}