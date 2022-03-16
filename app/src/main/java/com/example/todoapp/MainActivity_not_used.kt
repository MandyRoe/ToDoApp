package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.todoapp.authentification.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity_not_used : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    // lateinit means "we are doing that later"
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainnotused)

        //toggle home button functionality
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //nav items functionality
        nav_view.bringToFront()
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))

                R.id.nav_logout -> {
                    auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))

                R.id.nav_share -> Toast.makeText(
                    applicationContext,
                    "Clicked share",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.nav_test_change_activity -> startActivity(
                    Intent(
                        this,
                        CalendarActivity::class.java
                    )
                )


            }
            true
        }

       /* //toDo List

        todoAdapter = ToDoAdapter(mutableListOf())
        rvToDoItems.adapter = todoAdapter
        rvToDoItems.layoutManager = LinearLayoutManager(this)

        //add the click listeners to listen if the button gets clicked
        btnAddToDo.setOnClickListener {
            val todoTitle = etToDoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = ToDo(todoTitle)
                todoAdapter.addToDoDo(todo)
                // clear the field after the item was added
                etToDoTitle.text.clear()
            }
        }
        btnDeleteDone.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }
        //toDo List end*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //don't close app if drawer is open and back is pressed
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        return super.onBackPressed()
    }
}