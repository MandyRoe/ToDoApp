package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.authentification.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // lateinit means "we are doing that later"
    private lateinit var todoAdapter: ToDoAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        todoAdapter = ToDoAdapter(mutableListOf())
        rvToDoItems.adapter = todoAdapter
        rvToDoItems.layoutManager = LinearLayoutManager(this)

        //add the click listeners to listen if the button gets clicked
        btnAddToDo.setOnClickListener{
            val todoTitle = etToDoTitle.text.toString()
            if(todoTitle.isNotEmpty()) {
                val todo = ToDo(todoTitle)
                todoAdapter.addToDoDo(todo)
                // clear the field after the item was added
                etToDoTitle.text.clear()
            }
        }
        btnDeleteDone.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }

        btn_logout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}