package com.karimsinouh.youtixv2.utils

import android.content.Context
import androidx.core.content.edit

object FirstTime {


    private const val FIRST_TIME_PREFS="first_time_prefs"
    private const val FIRST_TIME_KEY="fir"

    fun isFirstTime(context: Context):Boolean{
        val prefs=context.getSharedPreferences(FIRST_TIME_PREFS,Context.MODE_PRIVATE)
        return prefs.getBoolean(FIRST_TIME_KEY,true)
    }
    fun isFirstTime(context: Context,value:Boolean){
        val prefs=context.getSharedPreferences(FIRST_TIME_PREFS,Context.MODE_PRIVATE)
        prefs.edit {
            putBoolean(FIRST_TIME_KEY,value)
            commit()
        }
    }

}