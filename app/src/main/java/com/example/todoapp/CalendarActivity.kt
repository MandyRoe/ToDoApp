package com.example.todoapp

import android.content.Intent
import android.icu.text.CaseMap
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // initialize variables
        val calendar = Calendar.getInstance()
        val tvTitle = findViewById<EditText>(R.id.tv_event_title)
        val tvLocation = findViewById<EditText>(R.id.tv_location)
        val tvDescription = findViewById<EditText>(R.id.tv_description)
        val btnAdd = findViewById<Button>(R.id.btn_add_events)

        // set btn to clickListener to activate it; set the time to now and 30min in milliseconds
        btnAdd.setOnClickListener {
            addEvent(tvTitle.text.toString(), tvLocation.text.toString(), tvDescription.text.toString(), calendar.timeInMillis, calendar.timeInMillis + 1800000)

        }
    }

    fun addEvent(title: String, location: String, description: String, begin: Long, end: Long){

        // intent to set the data for the calendar
        val intent = Intent(Intent.ACTION_INSERT).apply {
            // to transfer the data later into the calendar
            data = CalendarContract.Events.CONTENT_EXCEPTION_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            putExtra(CalendarContract.Events.DESCRIPTION, description)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
            type = "vnd.android.cursor.dir/event"
        }

        // call the calendar function, if there is a calendar function
        if(intent.resolveActivity(packageManager) != null)
            startActivity(intent)

    }
}