package com.example.todoapp


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_todo.view.*



class ToDoAdapter(private val todoList : ArrayList<ToDo>) : RecyclerView.Adapter<ToDoAdapter.TodoViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    var selectedTodo : String ?= null
    private var listData: MutableList<ToDo> = todoList as MutableList<ToDo>
    private lateinit var context : Context

    inner class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        //fetched info

        fun bind (todo: ToDo, index: Int){


        val title = itemView.findViewById<TextView>(R.id.tvTodoTitle)
        val description = itemView.findViewById<TextView>(R.id.tvDescription)
        val btn_delete = itemView.findViewById<Button>(R.id.btn_delete_todo)
        val btn_share = itemView.findViewById<Button>(R.id.btn_share_todo)
        val uid = auth.currentUser?.uid



        context = super.itemView.context             //context for start activity intent
        title.text = todo.title
        description.text = todo.description



            btn_delete.setOnClickListener{
                deleteItem(index)
                databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
                databaseReference.child(title.text.toString() + uid.toString()).removeValue()

            }

            btn_share.setOnClickListener{

                val intent = Intent(context, SelectUsersActivity::class.java).apply{
                    putExtra("title", title.text.toString() )                       //send data to other activity
                    putExtra("description", description.text.toString())

                }
                startActivity(context, intent, null)


            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return todoList.size
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(listData[position], position)

        /* val currentItem = todoList[position]
         holder.title.text = currentItem.title
         println(currentItem.title)
         //fetched info
         holder.itemView.apply {
             cbDone.isChecked = currentItem.check!!
             toggleCheck(tvTodoTitle, currentItem.check!!, currentItem.title!!, currentItem.uid!!)
             cbDone.setOnCheckedChangeListener { _, isChecked ->
                 toggleCheck(tvTodoTitle, isChecked, currentItem.title!!, currentItem.uid!!)
                 currentItem.check = !currentItem.check!!
             }
         }*/

    }

    fun deleteItem(index: Int){
        listData.removeAt(index)
        notifyDataSetChanged()
    }



    // checked or unchecked items. based on binary (paintFlags)
    private fun toggleCheck(tvToDoTitle: TextView, isChecked: Boolean, todoTitle : String, uid:String) {
        auth = FirebaseAuth.getInstance()

        if(isChecked == true) {

            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
            selectedTodo = todoTitle + uid
            println("*******" + selectedTodo + "is selected todo")


        } else {
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()

        }
    }



}