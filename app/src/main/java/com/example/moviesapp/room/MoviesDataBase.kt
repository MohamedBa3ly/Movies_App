package com.example.moviesapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.RemoteKeys

@Database(entities = [MovieDetails::class,RemoteKeys::class], version = 1, exportSchema = false)
abstract class MoviesDataBase: RoomDatabase() {

    abstract fun moviesDao():MoviesDao
    abstract fun remoteDao():RemoteDao

    companion object{
        @Volatile
        private var Instance : MoviesDataBase?=null
        fun getInstance(context: Context):MoviesDataBase{
            val tempInstance = Instance
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    MoviesDataBase::class.java,
                    "Movies_DataBase").build()
                Instance = instance
                return instance
            }
        }
    }


}