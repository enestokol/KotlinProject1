package com.example.kotlinproject1.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinproject1.Authentication.User
import com.example.kotlinproject1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.first_fragment_layout.*

class ProfileInfo : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentContent = inflater.inflate(R.layout.first_fragment_layout, container, false)
        readFBUser()
        return fragmentContent
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

    private fun readFBUser() {
        var reference = FirebaseDatabase.getInstance().reference
        var fbuser = FirebaseAuth.getInstance().currentUser
        val auth: FirebaseAuth=FirebaseAuth.getInstance()

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
                        Picasso.with(activity).load(user?.imageup).into(profile_image1)
                    }
                    else{
                        profile_image1.setImageResource(getResourceId(user?.image))
                    }
                    //profile_image1.setImageResource(getResourceId(user?.image))
                    uPw.text="ID: "+ user?.user_id
                    profile_user.text="Username: "+ user?.username
                    nPw1.text="Email: "+ auth.currentUser?.email.toString()
                    val fdate = java.text.SimpleDateFormat("yyyy-MM-dd")
                    val date = java.util.Date(auth.currentUser!!.metadata!!.lastSignInTimestamp)
                    nPw2.text="Last Login: "+ fdate.format(date)
                }
            }
        })
    }
}