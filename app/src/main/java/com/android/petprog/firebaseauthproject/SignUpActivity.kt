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


class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpMail: EditText
    private lateinit var signUpPassword: EditText
    private lateinit var signUpButton: Button

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpMail = findViewById(R.id.SignUpMail)
        signUpPassword = findViewById(R.id.SignUpPass)
        signUpButton = findViewById(R.id.SignUpButton)

        auth = Firebase.auth

        signUpButton.setOnClickListener {
            val email = signUpMail.text.toString()
            val password = signUpPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your E-mail address", Toast.LENGTH_LONG).show()
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter your Password", Toast.LENGTH_LONG).show()
            }
            if (password.length == 0) {
                Toast.makeText(this, "Please enter your Password", Toast.LENGTH_LONG).show()
            }
            if (password.length < 8) {
                Toast.makeText(this, "Password must be more than 8 digit", Toast.LENGTH_LONG).show()
            } else {
                //                auth?.createUserWithEmailAndPassword
                auth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(Intent(this, EditProfileActivity::class.java))
                            finish()
                        }
                    }
            }

        }

    }

    fun navigateSignIn(v: View?) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}
