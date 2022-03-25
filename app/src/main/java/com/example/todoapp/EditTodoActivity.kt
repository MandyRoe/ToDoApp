package com.example.todoapp

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edittodo.*
import kotlinx.android.synthetic.main.item_todo.view.*
import java.time.LocalDate
import java.time.LocalDateTime

class EditTodoActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edittodo)





        readTodoData()



        val btnEditTodo = findViewById<Button>(R.id.btnEditTodo)
        btnEditTodo.setOnClickListener {

            addTodoData()
            startActivity(Intent(this,ToDoActivity::class.java))
            Toast.makeText(this, "Activity edited", Toast.LENGTH_SHORT).show()


        }



    }

    private fun readTodoData(){

        val title = intent.getStringExtra("title").toString()
        val description = intent.getStringExtra("description").toString()
        val dueDate = intent.getStringExtra("dueDate").toString()

        val edTitle = findViewById<EditText>(R.id.etEditTodoTitle)
        edTitle.setText(title, TextView.BufferType.EDITABLE)

        val edDescription = findViewById<EditText>(R.id.etEditDescription)
        edDescription.setText(description, TextView.BufferType.EDITABLE)

        val edDueDate = findViewById<EditText>(R.id.etEditDueDate)
        edDueDate.setText(dueDate, TextView.BufferType.EDITABLE)




    }


    //adds todo item to DB
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addTodoData() {
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")

        val uid = auth.currentUser?.uid
        val todoTitle = etEditTodoTitle.text.toString()
        val description = etEditDescription.text.toString()
        val dueDate = LocalDate.parse(etEditDueDate.text.toString()).toString()
        val createdDate= intent.getStringExtra("createdDate").toString()

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {

                    if(ds.key == todoTitle + uid) {

                        val d = ds.child("done").value
                        val dd = ds.child("doneDate").value.toString()

                        val todo = ToDo(todoTitle, description, uid, d as Boolean?,dd, dueDate, createdDate)

                        databaseReference.child(todoTitle + uid).setValue(todo)


                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                //not needed
            }


        })







    }


}