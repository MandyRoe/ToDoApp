package com.example.todoapp


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.ToDo
import com.example.todoapp.authentification.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class ToDoActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var todoArrayList: ArrayList<ToDo>
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
        todoRecyclerView = findViewById(R.id.rvToDoItems)
        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.setHasFixedSize(true)

        todoArrayList = arrayListOf<ToDo>()

        getTodoData(uid!!)


        val btnAddToDo= findViewById<Button>(R.id.btnAddToDo)
        btnAddToDo.setOnClickListener {
            println("button press successfull")

            addTodoData()
            etToDoTitle.text.clear()

        }



        //toggle home button functionality
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

    } //onCreate end

     //functions

    private fun addTodoData() {
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val todoTitle = etToDoTitle.text.toString()
        val todo = ToDo(todoTitle, uid)

        databaseReference.child(todoTitle + uid).setValue(todo).addOnCompleteListener {

            getTodoData(uid!!)
            todoArrayList.clear()

        }
    }


    private fun getTodoData(uid: String) {

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
        databaseReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {
                    for (todoSnapshot in snapshot.children) {

                        if (todoSnapshot.key!!.contains(uid)) {             //nur vom jeweiligen nutzer sollen todos angezeigt werden
                            println("entered if")
                            val todo = todoSnapshot.getValue(ToDo::class.java)


                            todoArrayList.add(todo!!)                              //arrayList with all the todoitems in Database
                            todoRecyclerView.adapter = ToDoAdapter(todoArrayList)

                        } else println("uid doesnt match")


                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        println("get todo ausgeführt")

    }








    // hamburger button functionality
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




