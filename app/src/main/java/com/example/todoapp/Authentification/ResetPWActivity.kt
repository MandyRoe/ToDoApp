package com.example.todoapp.authentification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todoapp.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_resetpwactivity.*

/**
 * Activity to handle password reset with Firebase
 **/


class ResetPWActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpwactivity)

        //Initialize Firebase auth
        auth = FirebaseAuth.getInstance()


        btn_change_password.setOnClickListener {
            changePassword()
        }

    }

    //function to change password
    private fun changePassword() {

        // check if all fields are filled
        if (tv_confirm_password.text.isNotEmpty() &&
            tv_current_password.text.isNotEmpty() &&
            tv_new_password.text.isNotEmpty()
        ) {

            // check if confirm and new password are the same
            if (tv_new_password.text.toString().equals(tv_confirm_password.text.toString())) {

                val user = auth.currentUser
                // check if the user is logged in to continue changing the password or start the login activity
                if (user != null && user.email != null) {
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, tv_current_password.text.toString())

                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Re-Authentication was successful",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                // set the new password
                                user!!.updatePassword(tv_new_password.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Password changed successfully",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()

                                            // sign Out the user and bring him back to the Login
                                            auth.signOut()
                                            startActivity(Intent(this, LoginActivity::class.java))
                                            finish()
                                        }
                                    }
                            } else {
                                //if re-authentication failed, display text
                                Toast.makeText(this, "Re-Authentication failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    //if user is not currently logged in, start LoginActivity to start log ing
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            } else {
                //if entered passwords are not the same
                Toast.makeText(
                    this,
                    "New and Confirm Password are not the same",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            //if not all fields are filled
            Toast.makeText(this, "Please Enter all fields", Toast.LENGTH_SHORT)
                .show()
        }
    }
}