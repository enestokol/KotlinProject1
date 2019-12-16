package com.example.kotlinproject1.Game

import android.content.Context
import android.os.CountDownTimer

interface CardInterface {

    interface Presenter {
        fun fillInitial()
        fun fillList()
        fun getAdapter() : CardAdapter
        fun beginGame()
    }

    interface View {
        fun getContext() : Context
        fun refreshData(position: Int)
        fun updateScore(value: Int)
        fun showEnding()
        fun startGame()
        fun endTimer()
    }
}