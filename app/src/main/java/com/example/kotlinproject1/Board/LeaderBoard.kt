package com.example.kotlinproject1.Board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject1.Authentication.User
import com.example.kotlinproject1.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.leader_row_layout.view.*

class LeaderBoard : AppCompatActivity() {

    private fun getResourceId(id: Int) = when (id) {
        0 -> R.mipmap.ic_first_avatar
        1 -> R.mipmap.ic_second_avatar
        2 -> R.mipmap.ic_third_avatar
        3 -> R.mipmap.ic_fourth_avatar
        4 -> R.mipmap.ic_fifth_avatar
        5 -> R.mipmap.ic_sixth_avatar
        else -> R.mipmap.ic_first_avatar
    }

    lateinit var mRecyclerView:RecyclerView
    lateinit var mDatabase:DatabaseReference
    lateinit var query:Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaders)

        mRecyclerView=findViewById(R.id.list_view)
        mDatabase=FirebaseDatabase.getInstance().getReference("User")
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        loadFBData()
    }

    private fun loadFBData() {
        query = mDatabase.orderByChild("score").limitToLast(50)
        val FirebaseRecyclerAdapter= object : FirebaseRecyclerAdapter<User,UsersViewHolder>(
            User::class.java,
            R.layout.leader_row_layout,
            UsersViewHolder::class.java,
            query
        ){
            override fun populateViewHolder(p0: UsersViewHolder?, p1: User?, p2: Int) {
                p0?.image?.setImageResource(getResourceId(p1!!.image!!))
                p0?.name?.text=(p1!!.username)
                p0?.score?.text=((-1*p1.score!!).toString())
            }
        }
        mRecyclerView.adapter=FirebaseRecyclerAdapter
    }

    class UsersViewHolder(mview:View):RecyclerView.ViewHolder(mview){
        val image = mview.leaderImage
        val name = mview.leaderName
        val score = mview.leaderScore
    }
}
