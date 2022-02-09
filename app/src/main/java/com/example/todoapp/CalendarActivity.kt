package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // p0: it is calendar instance. p1: it’s selected year. p2: it’s selected month. p3: and It’s selected day.
        calendarView.setOnDateChangeListener { p0, p1, p2, p3 ->
            Toast.makeText(
                this@CalendarActivity,
                "The selected date is $p3.${p2 + 1}.$p1",
                Toast.LENGTH_SHORT
            ).show()
        }

        val backButton = findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener{
            val Intent = Intent(this,DashboardActivity::class.java)
            startActivity(Intent)
        }

    }

}