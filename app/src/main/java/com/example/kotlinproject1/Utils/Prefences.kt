package com.example.kotlinproject1.Utils

import android.content.Context

class Prefences(context:Context) {


    val prefence_name="last_score"
    val prefence_score="0"

    val prefence=context.getSharedPreferences(prefence_name,Context.MODE_PRIVATE)


    fun getLastScore():Int{
        return prefence.getInt(prefence_score,0)
    }

    fun setLastScore(score:Int){
        val editor=prefence.edit()
        editor.putInt(prefence_score,score)
        editor.apply()
    }

}

