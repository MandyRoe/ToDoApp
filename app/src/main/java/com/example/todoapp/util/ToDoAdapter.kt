package com.example.todoapp


import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_todo.view.*



class ToDoAdapter(private val todoList : ArrayList<ToDo>) : RecyclerView.Adapter<ToDoAdapter.TodoViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth : FirebaseAuth


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = todoList[position]
        holder.title.text = currentItem.title
        println(currentItem.title)
        //fetched info
        holder.itemView.apply {
            cbDone.isChecked = currentItem.check
            toggleConsequences(tvToDoTitle, currentItem.check, currentItem.title!!)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleConsequences(tvToDoTitle, isChecked, currentItem.title!!)
                currentItem.check = !currentItem.check
            }
        }

    }

    // checked or unchecked items. based on binary (paintFlags)
    private fun toggleConsequences(tvToDoTitle: TextView, isChecked: Boolean, todoTitle : String) {
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        if(isChecked == true) {
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
            databaseReference.child(todoTitle + uid).child("check").setValue(true).addOnCompleteListener{ //set DB value to true


            }


        } else {
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            databaseReference.child(todoTitle + uid).child("check").setValue(false)  //set DB value to false



        }
    }




    override fun getItemCount(): Int {

        return todoList.size
    }



    class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val title : TextView = itemView.findViewById(R.id.tvToDoTitle)
        //fetched info

    }




}