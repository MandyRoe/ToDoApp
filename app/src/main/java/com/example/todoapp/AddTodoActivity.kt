package com.example.todoapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_addtodo.*
import java.time.LocalDate
import java.time.LocalDateTime

class AddTodoActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addtodo)



        val btnCreateTodo = findViewById<Button>(R.id.btnCreateTodo)
        btnCreateTodo.setOnClickListener {

            addTodoData()
            startActivity(Intent(this,ToDoActivity::class.java))
            Toast.makeText(this, "Activity added", Toast.LENGTH_SHORT).show()


        }



    }
    //adds todo item to DB
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addTodoData() {
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")

        val uid = auth.currentUser?.uid
        val todoTitle = etTodoTitle.text.toString()
        val description = etDescription.text.toString()
        val dueDate = LocalDate.parse(etDueDate.text.toString()).toString()
        val createdDate = LocalDateTime.now().toString()
        val todo = ToDo(todoTitle, description, uid, false,"null", dueDate, createdDate)

        databaseReference.child(todoTitle + uid).setValue(todo)



    }


}