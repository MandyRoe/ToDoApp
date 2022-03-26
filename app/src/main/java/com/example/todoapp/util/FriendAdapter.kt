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
//import kotlinx.android.synthetic.main.item_todo.view.cbDone
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Adapter to create the recycler view for FriendActivity
 **/

//Adapter gets Array List of User items to display from FriendActivity
class FriendAdapter constructor(private val friendUserList: ArrayList<User>) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {


    private lateinit var context: Context
    private var listData: MutableList<User> = friendUserList as MutableList<User>

    //View Holder for the recycler view
    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //link name to be displayed on the "friend_add" layout
        val name: TextView = itemView.findViewById(R.id.tvFriendName)

        //bind values to current user item in array list (to know which item got interacted with in the recycler view and use the correct values)
        fun bind(user: User, index: Int) {
            context = super.itemView.context

            //initialize name to show first name and last name of respective user in text view field of the item
            name.text = user.firstName.toString() + " " + user.lastName.toString()


        }


    }
    //necessary functions below :)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)

        return FriendViewHolder(itemView)


    }


    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(listData[position], position)


    }


    override fun getItemCount(): Int {

        return friendUserList.size
    }


}