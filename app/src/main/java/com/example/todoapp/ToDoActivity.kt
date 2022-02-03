package com.example.todoapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.ToDo
import com.example.todoapp.authentification.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class ToDoActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_todo)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference =
                FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("ToDo")



        val saveButton = findViewById<Button>(R.id.btnAddToDo)
        saveButton.setOnClickListener {
            println("button press successfull")

            val toDoTitle = findViewById<EditText>(R.id.etToDoTitle)


            val toDoTitleString = toDoTitle.text.toString()
            println(toDoTitleString)




            val todo = ToDo(toDoTitleString, uid)
            if(uid != null){
                println(uid)

                databaseReference.child(toDoTitleString + uid).setValue(todo).addOnCompleteListener {

                    if (it.isSuccessful){
                        println("enter db successfull")

                    }else{
                        println("enter db not successfull")

                        Toast.makeText(this@ToDoActivity, "Failed to send toDo", Toast.LENGTH_SHORT).show()
                    }

                }
            }


        }


        //toggle home button functionality
        lateinit var toggle: ActionBarDrawerToggle

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //nav items functionality
        nav_view.bringToFront()
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, DashboardActivity::class.java))

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
                        TestActivity::class.java
                    )
                )


            }
            true
        }




    }



}