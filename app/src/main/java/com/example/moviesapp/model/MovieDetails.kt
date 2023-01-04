package com.example.moviesapp.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class ApiDetailsResponse(
    val page:Int,
    var results: List<MovieDetails>
)

@Parcelize
@Entity(tableName = "movies_table")
data class MovieDetails(
    @PrimaryKey (autoGenerate = false) val id: Int,
    var page:Int=1,
    val title: String?,
    val overview: String?,
    val vote_average: Double?,
    val poster_path: String?,

):Parcelable {
    val urlImageMovie: String
        get() = "https://image.tmdb.org/t/p/w500${poster_path}"
}