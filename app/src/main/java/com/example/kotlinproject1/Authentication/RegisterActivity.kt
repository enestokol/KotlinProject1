package com.example.kotlinproject1.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kotlinproject1.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private fun newUser(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(object: OnCompleteListener<AuthResult>{
                override fun onComplete(p0: Task<AuthResult>) {
                    if(p0.isSuccessful){
                        val dbUser=User(user_id = auth.currentUser!!.uid,username =auth.currentUser?.email!!.substring(0,auth.currentUser?.email!!.indexOf("@")),image =(1..5).random(),score =0,imageup =null,highscore = 0)
                        FirebaseDatabase.getInstance().reference
                            .child("User")
                            .child(auth.currentUser!!.uid)
                            .setValue(dbUser).addOnCompleteListener{
                                if(it.isSuccessful){
                                    Toast.makeText(this@RegisterActivity,"Succeeded",Toast.LENGTH_SHORT).show()
                                    auth.signOut()
                                    var intent=Intent(this@RegisterActivity,LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        var intent=Intent(this@RegisterActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this@RegisterActivity,"An error occured"+p0.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener{
            if(rMail.text.isNotEmpty() && rPassword1.text.isNotEmpty() && rPassword2.text.isNotEmpty()){
                if(rPassword1.text.toString().equals(rPassword2.text.toString())){
                    newUser(rMail.text.toString(),rPassword1.text.toString())
                }
                else{
                    Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(this,"Fill in the blanks",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
