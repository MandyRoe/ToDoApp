package com.example.todoapp


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.item_todo.view.*
import java.time.LocalDateTime

/**
 * Adapter to create the recycler view for ToDoActivity
 **/


//Adapter gets Array List of ToDo items to display from TodoActivity, aswell as the due date and created date
class ToDoAdapter(
    private val dueDate: String,
    private val createdDate: String,
    private val todoList: ArrayList<ToDo>
) : RecyclerView.Adapter<ToDoAdapter.TodoViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var listData: MutableList<ToDo> = todoList as MutableList<ToDo>
    private lateinit var context: Context


    //View Holder for the recycler view
    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(todo: ToDo, index: Int) {
            //database Reference
            databaseReference =
                FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("ToDo")

            //link to layout of item_todo
            val uid = auth.currentUser?.uid
            val title = itemView.findViewById<TextView>(R.id.tvTodoTitle)
            val description = itemView.findViewById<TextView>(R.id.tvDescription)

            val btn_delete = itemView.findViewById<Button>(R.id.btn_delete_todo)
            val btn_share = itemView.findViewById<Button>(R.id.btn_share_todo)
            val btn_edit = itemView.findViewById<Button>(R.id.btn_edit_todo)


            //context for start activity intent
            context = super.itemView.context

            //initialize title and description
            title.text = todo.title
            description.text = todo.description


            //Delete button functionality
            btn_delete.setOnClickListener {
                //delete in recycler array
                deleteItem(index)
                //delete in database
                databaseReference.child(title.text.toString() + uid.toString()).removeValue()

            }

            //Share button functionality
            btn_share.setOnClickListener {

                //pass data to SelectUsersActivity
                val intent = Intent(context, SelectUsersActivity::class.java).apply {
                    putExtra("title", title.text.toString())
                    putExtra("description", description.text.toString())
                    putExtra("dueDate", dueDate)
                    putExtra("createdDate", createdDate)

                }
                //go to SelectUsersActivity
                startActivity(context, intent, null)


            }

            //Edit button functionality
            btn_edit.setOnClickListener {
                //pass data to EditTodoActivity
                val intent = Intent(context, EditTodoActivity::class.java).apply {
                    putExtra("title", title.text.toString())
                    putExtra("description", description.text.toString())
                    putExtra("dueDate", dueDate)
                    putExtra("createdDate", createdDate)

                }
                //go to EditTodoActivity
                startActivity(context, intent, null)

            }


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return todoList.size
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(listData[position], position)

        val currentItem = todoList[position]


        holder.itemView.apply {

            //show status of already existing todos (done or undone)
            auth = FirebaseAuth.getInstance()
            var uid = auth.currentUser?.uid
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //go through todo items in database
                    for (ds in snapshot.children) {

                        //if status of todo is set to done, and current user owns the todo
                        if (ds.child("done")
                                .getValue() == true && ds.key == currentItem.title.toString() + uid
                        ) {

                            //display background color  green(ish)
                            item_whole.setBackgroundColor(Color.parseColor("#41c9c5"))

                        }
                        //if  todo is not done yet, and current user owns the todo
                        else if (ds.child("done")
                                .getValue() == false && ds.key == currentItem.title.toString() + uid
                        ) {

                            //display background color white
                            item_whole.setBackgroundColor(Color.parseColor("#ffffff"))

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //not needed
                }


            })


            //update status on todos with click on check button
            btn_done_todo.setOnClickListener {

                auth = FirebaseAuth.getInstance()
                var uid = auth.currentUser?.uid
                val doneDate = LocalDateTime.now().toString()
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        //go through all todo items in database
                        for (ds in snapshot.children) {

                            //if current todo is not done and user owns the item
                            if (ds.child("done")
                                    .getValue() == false && ds.key == currentItem.title.toString() + uid
                            ) {
                                //update the database with current item information to done with current done date
                                databaseReference.child(currentItem.title.toString() + uid)
                                    .child("done").setValue(true)
                                databaseReference.child(currentItem.title.toString() + uid)
                                    .child("doneDate").setValue(doneDate)
                                //set background color of item to green(ish)
                                item_whole.setBackgroundColor(Color.parseColor("#41c9c5"))
                                //change icon to undo
                                btn_done_todo.setBackgroundResource(R.drawable.undo_icon)

                            }
                            //if current todo is done and user owns the item
                            else if (ds.child("done")
                                    .getValue() == true && ds.key == currentItem.title.toString() + uid
                            ) {
                                //set background color of item to white
                                databaseReference.child(currentItem.title.toString() + uid)
                                    .child("done").setValue(false)
                                databaseReference.child(currentItem.title.toString() + uid)
                                    .child("doneDate").setValue("null")
                                item_whole.setBackgroundColor(Color.parseColor("#ffffff"))
                                //change icon to check
                                btn_done_todo.setBackgroundResource(R.drawable.check_icon)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //not needed
                    }


                })


            }
        }

    }

    fun deleteItem(index: Int) {
        listData.removeAt(index)
        notifyDataSetChanged()
    }


}