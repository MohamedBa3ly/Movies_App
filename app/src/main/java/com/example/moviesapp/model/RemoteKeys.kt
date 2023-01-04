package com.example.moviesapp.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keys_table")

data class RemoteKeys(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "movie_id")
    val movieId: Int ,
    var prevPage:Int?,
    var currentPage:Int?,
    var nextPage:Int?,
    @ColumnInfo(name = "created_at")
    var createdAt: Long = System.currentTimeMillis()
)
