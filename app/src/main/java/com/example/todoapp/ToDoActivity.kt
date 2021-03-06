package com.example.todoapp
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.authentification.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlin.collections.ArrayList


class ToDoActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var todoArrayList: ArrayList<ToDo>
    lateinit var selectedTodoArrayList: ArrayList<ToDo>
    private lateinit var toggle: ActionBarDrawerToggle


    @RequiresApi(Build.VERSION_CODES.O)
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
        selectedTodoArrayList = arrayListOf<ToDo>()


        readTodoData(uid!!)


        val btnAddToDo= findViewById<Button>(R.id.btnAddToDo)
        btnAddToDo.setOnClickListener {
            println("add pressed successfully")

            startActivity(Intent(this, AddTodoActivity::class.java))


        }


        //toggle home button functionality
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //nav items functionality - if item is clicked start corresponding activity
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

                R.id.nav_friends -> startActivity(Intent(this, FriendActivity::class.java))


                R.id.nav_calendar -> startActivity(
                    Intent(
                        this,
                        CalendarActivity::class.java
                    )
                )

                R.id.nav_analysis -> startActivity(Intent(this, AnalysisActivity::class.java))


            }
            true
        }

    } //onCreate end


    //functions to fetch user owned todo items
    @RequiresApi(Build.VERSION_CODES.O)
    private fun readTodoData(uid: String) {

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                    //loop through all todos in database
                    for (todoSnapshot in snapshot.children) {
                        //select self owned items
                        if (todoSnapshot.key!!.contains(uid)) {
                            val todo = todoSnapshot.getValue(ToDo::class.java)
                            val cDate = todoSnapshot.child("createdDate").getValue().toString()
                            val dDate = todoSnapshot.child("dueDate").getValue().toString()
                            //add item to arraylist
                            todoArrayList.add(todo!!)
                            //pass arraylist with additional info to adapter
                            todoRecyclerView.adapter = ToDoAdapter(dDate, cDate, todoArrayList)

                        }

                    }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

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



