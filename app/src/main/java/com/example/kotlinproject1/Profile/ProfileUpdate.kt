package com.example.kotlinproject1.Profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlinproject1.Authentication.LoginActivity
import com.example.kotlinproject1.Authentication.User
import com.example.kotlinproject1.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.second_fragment_layout.*
import kotlinx.android.synthetic.main.second_fragment_layout.nPw1
import kotlinx.android.synthetic.main.second_fragment_layout.nPw2
import kotlinx.android.synthetic.main.second_fragment_layout.profile_image1
import kotlinx.android.synthetic.main.second_fragment_layout.uPw1


class ProfileUpdate : Fragment() {
    private var auth: FirebaseAuth=FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateBtn.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        if(uPw1.text.isNotEmpty() && nPw1.text.isNotEmpty() && nPw2.text.isNotEmpty()){
            if(nPw1.text.toString().equals(nPw2.text.toString())){
                val user = auth.currentUser
                if(user!=null && user.email!=null){
                    val credential = EmailAuthProvider.getCredential(user.email!!, uPw1.text.toString())
                    user.reauthenticate(credential).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(activity, "Re-Authentication success.", Toast.LENGTH_SHORT).show()
                            user.updatePassword(nPw1.text.toString())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) { Toast.makeText(activity, "Password changed successfully.", Toast.LENGTH_SHORT).show()
                                        auth.signOut()
                                        startActivity(Intent(activity, LoginActivity::class.java))
                                    }
                                    else{
                                        Toast.makeText(activity,"Re-Auth failed",Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
                }else{startActivity(Intent(activity, LoginActivity::class.java))}
            }else {Toast.makeText(activity, "Password mismatching.", Toast.LENGTH_SHORT).show()}
        }else {Toast.makeText(activity, "Please enter all the fields.", Toast.LENGTH_SHORT).show()}
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

        var query = reference.child("User")
            .orderByKey()
            .equalTo(fbuser?.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                 for (singleSnapshot in p0.children) {
                    var user = singleSnapshot.getValue(User::class.java)
                    profile_image1.setImageResource(getResourceId(user?.image))
                }

            }

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentContent = inflater.inflate(R.layout.second_fragment_layout, container, false)

        readFBUser()

        return fragmentContent
    }
}