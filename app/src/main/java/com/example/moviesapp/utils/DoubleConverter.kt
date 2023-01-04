package com.example.moviesapp.utils

import androidx.databinding.InverseMethod

object DoubleConverter {

    @InverseMethod("doubleToFloat")
    @JvmStatic
    fun doubleToFloat(value: Double): Float{
        return value.toFloat()
    }
}