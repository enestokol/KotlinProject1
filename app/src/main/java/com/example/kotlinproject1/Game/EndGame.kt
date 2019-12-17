package com.example.kotlinproject1.Game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinproject1.Authentication.User
import com.example.kotlinproject1.MainActivity
import com.example.kotlinproject1.R
import com.example.kotlinproject1.Utils.Prefences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_end_game.*

class EndGame : AppCompatActivity() {

    var lhigh=0

    private fun readFBUser(context: Context) {
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
                    val pref=Prefences(context)

                    FirebaseAuth.getInstance().currentUser?.uid?.let {
                        reference.child("User").child(it).child("score")
                            .setValue( user?.score!!-pref.getLastScore() )
                    }

                    FirebaseAuth.getInstance().currentUser?.uid?.let {
                        reference.child("User").child(it).child("highscore")
                            .setValue( lhigh)
                    }
                }
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)
        val intent=intent
        val score=intent.getStringExtra("Score")
        val pref=Prefences(this)




        if(pref.getLastScore()!=null){
            if(score!!.toInt()>=pref.getLastScore()){
                lhigh=score.toInt()
            }
            else{
                lhigh=pref.getLastScore()
            }
        }
        else{
            lhigh=score!!.toInt()
        }


        pref.setLastScore(score.toInt())



        uEndScore.text="Score: "+score.toString()
        readFBUser(context = this)

        re_game.setOnClickListener {
            val intent1= Intent(this@EndGame,GameActivity::class.java)
            startActivity(intent1)
            finish()
        }

        homepage.setOnClickListener {
            val intent2=Intent(this@EndGame,MainActivity::class.java)
            //intent2.putExtra("lastscore",lhigh.toString())
            startActivity(intent2)
            finish()
        }

    }
}
