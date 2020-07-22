package com.android.petprog.firebaseauthproject

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var signInMail: EditText
    private lateinit var signInPassword: EditText
    private lateinit var signInButton: Button
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInMail = findViewById(R.id.SignInMail)
        signInPassword = findViewById(R.id.SignInPass)
        signInButton = findViewById(R.id.SignInButton)

        auth = Firebase.auth


        signInButton.setOnClickListener {
            val email = signInMail.text.toString()
            val password = signInPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter your mail address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        if (password.length < 8) {
                            Toast.makeText(
                                this,
                                "Password must be more than 8 digit",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
        }
    }

    fun navigateSignUp(v: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun navigateForgetMyPassword(v: View?) {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }
}
