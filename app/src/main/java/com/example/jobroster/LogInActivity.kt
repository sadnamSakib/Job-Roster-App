package com.example.jobroster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobroster.databinding.LoginlayoutBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = Firebase.auth
        binding.loginbutton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(baseContext, "Authentication failed.:" +task.exception?.message ,Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}