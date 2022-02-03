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

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)



        dash_todo.setOnClickListener {
           startActivity(Intent(this, ToDoActivity::class.java))
        }

        dash_events.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
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
}

