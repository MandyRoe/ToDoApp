package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.todoapp.authentification.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.tv_email
import kotlinx.android.synthetic.main.activity_login.tv_password
import kotlinx.android.synthetic.main.activity_register.*

/**
 * Activity to handle user registration with Firebase
 **/


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Register button functionality to register user
        btn_register.setOnClickListener {
            signUpUser()
        }
    }

    //function to register user with Firebase
    private fun signUpUser() {
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


        auth.createUserWithEmailAndPassword(tv_email.text.toString(), tv_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext, "created account successfully !",
                        Toast.LENGTH_SHORT
                    ).show()

                    val user = auth.currentUser
                    // if the account is created we need to verify the email address
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val emptyUser = User("not set", "not set", "not set")
                                val uid = auth.currentUser?.uid
                                //functionality to send user to profile after registration in order to soft force user creation in database
                                databaseReference =
                                    FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app")
                                        .getReference("Users")
                                databaseReference.child(uid!!).setValue("reg_flag")
                                databaseReference.child(uid!!).child("reg_flag").setValue(true)


                                // Sign in success, update UI with the signed-in user's information
                                startActivity(Intent(this, LoginActivity::class.java))

                                finish()
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Sign Up failed. Try again",
                        Toast.LENGTH_SHORT
                    ).show()


                }
            }
    }


}