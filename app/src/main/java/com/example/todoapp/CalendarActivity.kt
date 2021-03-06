package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.authentification.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_calendar.drawerLayout
import kotlinx.android.synthetic.main.activity_calendar.nav_view
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Activity to display calendar and show todo items in recylcer view according to due date
 **/



class CalendarActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var todoArrayList: ArrayList<ToDo>
    lateinit var selectedTodoArrayList: ArrayList<ToDo>
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
        todoRecyclerView = findViewById(R.id.rvToDoItems)
        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.setHasFixedSize(true)
        todoArrayList = arrayListOf<ToDo>()
        selectedTodoArrayList = arrayListOf<ToDo>()







        // _: it is calendar instance. year: it’s selected year. month: it’s selected month. day: and It’s selected day.
        calendarView.setOnDateChangeListener { _, year, month, day ->

            readTodoDate(uid!!,year.toString(),ddMonth(month+1),ddDay(day))
            todoArrayList.clear()
            todoRecyclerView.adapter?.notifyDataSetChanged()

        }


        //back button functionality
        val backButton = findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener{
            val Intent = Intent(this,MainActivity::class.java)
            startActivity(Intent)
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



    }
    //parse from possibly single digit to double digit String
    private fun ddMonth(month : Int): String {
        if (month <10){
            return "0"+month
        }
        else return ""+month
    }
    //parse from possibly single digit to double digit String
    private fun ddDay(day : Int) : String{
        if (day <10){
            return "0"+day
        }
        else return ""+day
    }

    //read todos with due date on selected day
    private fun readTodoDate(uid: String, y :String, m :String, d:String ) {

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {   //addValueEventListener would loop infinite

            override fun onDataChange(snapshot: DataSnapshot) {

                //loop through all todos in database
                for (todoSnapshot in snapshot.children) {

                    //check if user owns item in db
                    if (todoSnapshot.key!!.contains(uid)) {                                  //only show user owned items
                        val todo = todoSnapshot.getValue(ToDo::class.java)
                        val cDate = todoSnapshot.child("createdDate").getValue().toString()
                        val dDate = todoSnapshot.child("dueDate").getValue().toString()

                        //check if due date is equal to clicked date on calendar
                        if (dDate == y +"-"+ m +"-"+ d){
                            todoArrayList.add(todo!!)                                            //arrayList with all the user owned todos in Database
                            todoRecyclerView.adapter = ToDoAdapter(dDate, cDate, todoArrayList)


                    }
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