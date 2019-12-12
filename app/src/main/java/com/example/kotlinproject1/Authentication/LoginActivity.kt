package com.example.kotlinproject1.Authentication

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kotlinproject1.MainActivity
import com.example.kotlinproject1.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        if (auth.getCurrentUser() != null) {
            var intent=Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerButton.setOnClickListener{
            var intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            //finish()
            super.onBackPressed()
        }

        loginButton.setOnClickListener {
            if(loginEmail.text.isNotEmpty() && loginPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail.text.toString(),loginPassword.text.toString())
                    .addOnCompleteListener(object: OnCompleteListener<AuthResult>{
                        override fun onComplete(p0: Task<AuthResult>) {
                            if(p0.isSuccessful){
                                var intent=Intent(this@LoginActivity,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this@LoginActivity,p0.exception?.message,Toast.LENGTH_SHORT).show()

                            }
                        }
                    })
            }
            else{
                Toast.makeText(this@LoginActivity,"Fill in the blank",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
