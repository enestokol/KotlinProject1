package com.example.kotlinproject1.Game
import android.graphics.Color
import android.os.Handler
import com.example.kotlinproject1.R
import com.example.kotlinproject1.Game.CardInterface.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class CardPresenter (view: View, level: Int =8) : Presenter {



    private var mView: View = view
    var itemsCount = level
    var point = 0
    private var items: ArrayList<Card> = arrayListOf()
    private var images: ArrayList<CardImage> = arrayListOf()
    var visibles = arrayListOf<Int>()
    private var isCLickable = true


    init {
        fillList()
    }

    override fun fillInitial() {
        var x = 0
        while (x < itemsCount) {
            val image = images[Random.nextInt(images.size)]
            if (countOf(Card(image)) < 2) {
                items.add(Card(image))
                items.add(Card(image))
                x+=2
            }
        }
        items.shuffle()
    }

    override fun beginGame() {
        fillInitial()
        mView.startGame()
        point = 0
    }





    override fun fillList() {
        images.add(CardImage(R.drawable.a,Color.parseColor("#4FC3F7")))
        images.add(CardImage(R.drawable.b,Color.parseColor("#F06292")))
        images.add(CardImage(R.drawable.c,Color.parseColor("#42A5F5")))
        images.add(CardImage(R.drawable.d,Color.parseColor("#e57373")))
        images.add(CardImage(R.drawable.e,Color.parseColor("#4DB6AC")))
        images.add(CardImage(R.drawable.f,Color.parseColor("#4DD0E1")))
        images.add(CardImage(R.drawable.g,Color.parseColor("#00B0FF")))
        images.add(CardImage(R.drawable.h,Color.parseColor("#2196F3")))
    }

    override fun getAdapter(): CardAdapter {
        var deckAdapter = CardAdapter(items, mView.getContext()) {
            if (isCLickable) {
                if (!items[it].isVisible) {
                    items[it].isVisible = true
                    visibles.add(it)
                    mView.refreshData(it)
                    if (visibles.size == 2) {
                        isCLickable = false
                        var handler = Handler()
                        handler.postDelayed({
                            if (items[visibles[0]].getImage() == items[visibles[1]].getImage()) {
                                items[visibles[0]].isMatched = true
                                items[visibles[1]].isMatched = true
                                point+=20
                                mView.updateSteps(point)
                            } else {
                                items[visibles[0]].isVisible = false
                                items[visibles[1]].isVisible = false
                            }
                            mView.refreshData(visibles[0])
                            mView.refreshData(visibles[1])
                            visibles.clear()
                            isCLickable = true
                            checkGameEnd()
                        }, 250L)

                    }

                }
            }
        }
        return deckAdapter
    }

    fun checkGameEnd() {
        var i = 0
        for(k in 0 until items.size) {
            if(items[k].isMatched) i++
        }
        if(i == items.size) {
            mView.showEnding()
            items.clear()
        }
    }

    fun countOf(item: Card): Int {
        return Collections.frequency(items, item)
    }
}