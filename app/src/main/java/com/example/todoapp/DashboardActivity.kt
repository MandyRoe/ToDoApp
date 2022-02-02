package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.todoapp.authentification.LoginActivity
import com.example.todoapp.authentification.ResetPWActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        dash_todo.setOnClickListener {
           startActivity(Intent(this, MainActivity::class.java))
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

    }
}