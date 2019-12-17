package com.example.kotlinproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinproject1.Authentication.LoginActivity
import com.example.kotlinproject1.Authentication.User
import com.example.kotlinproject1.Board.LeaderBoard
import com.example.kotlinproject1.Game.GameActivity
import com.example.kotlinproject1.Profile.ProfileActivity
import com.example.kotlinproject1.Utils.Prefences
import com.example.kotlinproject1.Utils.ProfilePhoto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth=FirebaseAuth.getInstance()
    private var latescore=0
   // private var lasthighscore=0

    private fun getResourceId(id: Int?) = when (id) {
        0 -> R.mipmap.ic_first_avatar
        1 -> R.mipmap.ic_second_avatar
        2 -> R.mipmap.ic_third_avatar
        3 -> R.mipmap.ic_fourth_avatar
        4 -> R.mipmap.ic_fifth_avatar
        5 -> R.mipmap.ic_sixth_avatar
        else -> R.mipmap.ic_first_avatar
    }

    private fun readFBUser() {
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
                if(user?.imageup!=null){
                    Picasso.with(this@MainActivity).load(user?.imageup).into(user_image)
                }
                else{
                    user_image.setImageResource(getResourceId(user?.image))
                }
                user_name.text="Welcome "+user?.username
                scores.text="Score: ${latescore}\nHigh Score: ${user?.highscore} "
            }
         }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        if (auth.currentUser == null) {
            val intent= Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



        val pref=Prefences(this)
        latescore=pref.getLastScore()
//        val intent2=intent
//        var score=intent2.getStringExtra("lastscore")
//
//        if(score==null){
//            score=latescore.toString()
//        }
//        lasthighscore=score.toInt()


        readFBUser()



        leaderboard.setOnClickListener {
            val intent= Intent(this@MainActivity,LeaderBoard::class.java)
            startActivity(intent)
        }

        game.setOnClickListener {
            val intent= Intent(this@MainActivity,GameActivity::class.java)
            startActivity(intent)
        }

        profile.setOnClickListener {
            val intent=Intent(this@MainActivity,ProfileActivity::class.java)
            startActivity(intent)
        }

        profilephoto.setOnClickListener {
            val intent=Intent(this@MainActivity,ProfilePhoto::class.java)
            startActivity(intent)
            finish()
        }

        signOutbtn.setOnClickListener {
            auth.signOut()
            pref.setLastScore(0)
            auth.addAuthStateListener {
                if (it.currentUser == null) {
                    val intent= Intent(this@MainActivity,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }
}
