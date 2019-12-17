package com.example.kotlinproject1.Game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlinproject1.Authentication.User
import com.example.kotlinproject1.R
import com.example.kotlinproject1.Game.CardInterface.View
import com.example.kotlinproject1.databinding.ActivityGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity(), View {

    private lateinit var presenter : CardPresenter
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)

        presenter = CardPresenter(this,8)
        readFBData()
        presenter.beginGame()
    }

    override fun getContext(): Context {
        return this
    }



    lateinit var t:CountDownTimer

    override fun startGame() {
        binding.deckRecycler.layoutManager = GridLayoutManager(this, 4)
        binding.deckRecycler.adapter = presenter.getAdapter()

        t=object : CountDownTimer(25000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.secs.text=(millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                binding.secs.text="0"
                endTimer()
            }
        }.start()
    }


    override fun endTimer() {
        if(binding.secs.text=="0") {
            showEnding()
        }
    }

        override fun showEnding() {

        binding.deckRecycler.visibility           = android.view.View.GONE
        binding.scoreText.visibility      = android.view.View.GONE
        binding.secs.visibility = android.view.View.GONE

        t.cancel()
        val intent = Intent(this,EndGame::class.java)
        intent.putExtra("Score",scoreText.text)
        startActivity(intent)
        finish()


    }

    override fun refreshData(position: Int) {
        binding.deckRecycler.adapter?.notifyItemChanged(position)
    }



    override fun updateScore(value: Int) {
        if(binding.secs.text!="0") {
            binding.scoreText.text = value.toString()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    private fun getResourceId(id: Int?) = when (id) {
        0 -> R.mipmap.ic_first_avatar
        1 -> R.mipmap.ic_second_avatar
        2 -> R.mipmap.ic_third_avatar
        3 -> R.mipmap.ic_fourth_avatar
        4 -> R.mipmap.ic_fifth_avatar
        5 -> R.mipmap.ic_sixth_avatar
        else -> R.mipmap.ic_first_avatar
    }

    private fun readFBData() {
        var reference = FirebaseDatabase.getInstance().reference
        var fbuser = FirebaseAuth.getInstance().currentUser


        var query = reference.child("User")
            .orderByKey()
            .equalTo(fbuser?.uid)


        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (singleSnapshot in p0.children) {
                    var user = singleSnapshot.getValue(User::class.java)
                    binding.gameUserImage.setImageResource(getResourceId(user?.image))
                }
            }

        })
    }


}