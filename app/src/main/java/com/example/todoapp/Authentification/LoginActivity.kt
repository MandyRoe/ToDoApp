package com.example.todoapp.authentification

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        btn_signup.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        btn_LoginButton.setOnClickListener{
            doLogin()
        }

        btn_forgotpw.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            // Show the Dialog field
            val view = layoutInflater.inflate(R.layout.screen_forgot_pw, null)
            val username = view.findViewById<EditText>(R.id.tv_email)
            builder.setView(view)

            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
            forgotPassword(username)
        })
            builder.setNegativeButton("Close", DialogInterface.OnClickListener{_, _ -> })
            builder.show()
        }
    }

    private fun forgotPassword(username: EditText) {
        if (username.text.toString().isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }
        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email sent.", Toast.LENGTH_SHORT).show()
                }
            }
    }



    private fun doLogin() {
        if (tv_email.text.toString().isEmpty()) {
            tv_email.error = "Enter your Email"
            tv_email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(tv_email.text.toString()).matches()) {
            tv_email.error = "Enter valid Email"
            tv_email.requestFocus()
            return
        }

        if (tv_password.text.toString().isEmpty()) {
            tv_email.error = "Enter your Password"
            tv_email.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(tv_email.text.toString(), tv_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
    // if the currentUser is not null - open the Dashboard/MainActivity
        if(currentUser != null) {
            // check if the user has verified his email address
            if(currentUser.isEmailVerified){
                startActivity(Intent(this, MainActivity::class.java))
            finish()
            }else{
            Toast.makeText(
                baseContext, "Please verify your E-Mail Address.",
                Toast.LENGTH_SHORT
            ).show()
            }
        }else{
            Toast.makeText(baseContext, "Login failed.",
                Toast.LENGTH_SHORT).show()
        }
    }

}