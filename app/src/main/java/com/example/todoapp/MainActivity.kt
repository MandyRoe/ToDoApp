package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todoapp.authentification.LoginActivity
import com.example.todoapp.authentification.ResetPWActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkReg()

        home_todo.setOnClickListener {
           startActivity(Intent(this, ToDoActivity::class.java))
        }

        home_events.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

        home_profile.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        home_analysis.setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }

        home_change_pw.setOnClickListener {
            startActivity(Intent(this, ResetPWActivity::class.java))
        }





        home_logout.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    //checks regflag to force profile creation after registration
    private fun checkReg() {
        auth = FirebaseAuth.getInstance()
        var uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.getChildren()) {
                    var checkReg = ds.child("reg_flag").getValue()

                    if(ds.key == uid && checkReg == true){
                        sendToProfile()

                    } else if(checkReg == null){

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

