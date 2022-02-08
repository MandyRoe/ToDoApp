package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.todoapp.authentification.LoginActivity
import com.example.todoapp.authentification.ResetPWActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        checkReg()

        dash_todo.setOnClickListener {
           startActivity(Intent(this, ToDoActivity::class.java))
        }

        dash_events.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

        dash_profile.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        dash_change_pw.setOnClickListener {
            startActivity(Intent(this, ResetPWActivity::class.java))
        }

        dash_change_pw.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }

        dash_logout.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    //checks regflag for forcing profile creation
    private fun checkReg() {
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.getChildren()) {
                    var checkReg = ds.child("reg_flag").getValue()

                    if(checkReg ==true){
                        sendToProfile()

                    } else if(checkReg == null){
                        println("reg_flag doesnt exist so user is registered")
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
    private fun sendToProfile(){
        startActivity(Intent(this, ProfileActivity::class.java))
    }

}

