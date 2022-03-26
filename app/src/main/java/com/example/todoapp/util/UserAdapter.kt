package com.example.todoapp


import android.content.Context
import android.content.Intent
import android.graphics.Paint.FAKE_BOLD_TEXT_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.protobuf.Value
import kotlinx.android.synthetic.main.item_todo.view.*

import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Adapter to create the recycler view for SelectUsersActivity
 **/

//Adapter gets Array List of User items to display, aswell as information of todo item to share
class UserAdapter constructor(private val title: String, private val descript: String, private val dueD: String, private val createdD: String, private val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var context: Context
    private var listData: MutableList<User> = userList as MutableList<User>


    //View Holder for the recycler view
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //link name to be displayed on the "friend_add" item
        val name: TextView = itemView.findViewById(R.id.tvUserName)

        //bind values to current user item in array list (to know which item got interacted with in the recycler view and use the correct values)
        fun bind(user: User, index: Int) {

            //link name and button to the item_user layout
            val name = itemView.findViewById<TextView>(R.id.tvUserName)
            val btnShareToUser = itemView.findViewById<Button>(R.id.btnShareToUser)

            context = super.itemView.context

            name.text = user.firstName.toString() + " " + user.lastName.toString()


            //share button functionality
            btnShareToUser.setOnClickListener {

                //database reference to todo path
                databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")

                val uid = user.uid.toString()

                //create todo object with passed information
                val todo = ToDo(title, descript, uid, false, dueD, createdD)

                //post todo with information of object to database
                databaseReference.child(title + uid).setValue(todo)

                //go back to todo list (toDoActivity)
                val intent = Intent(context, ToDoActivity::class.java)
                startActivity(context, intent, null)
                //toast feedback to user
                Toast.makeText(context, "Todo Shared with " + user.firstName + " " + user.lastName, Toast.LENGTH_SHORT).show()

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)

        return UserViewHolder(itemView)


    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listData[position], position)


    }


    override fun getItemCount(): Int {

        return userList.size
    }


}