package com.example.todoapp


import android.content.Context
import android.content.Intent
import android.graphics.Paint.FAKE_BOLD_TEXT_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Build
import android.system.Os.remove
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.protobuf.Value
import kotlinx.android.synthetic.main.item_friendrequest.view.*
import kotlinx.android.synthetic.main.item_todo.view.*
//import kotlinx.android.synthetic.main.item_todo.view.cbDone
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Adapter to create the recycler view for FriendRequestActivity
 **/

//Adapter gets Array List of FriendRequest items to display from FriendRequestActivity
class FriendRequestAdapter constructor(private val friendRequestList: ArrayList<FriendRequest>) : RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>() {

    private lateinit var context: Context
    private var listData: MutableList<FriendRequest> = friendRequestList as MutableList<FriendRequest>


    //View Holder for the recycler view
    inner class FriendRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //bind values to current FriendRequest item in array list (to know which item got interacted with in the recycler view and use the correct values)
        fun bind(request: FriendRequest, index: Int) {

            //link name and buttons to the "friendrequest" item
            val name: TextView = itemView.findViewById(R.id.tvFriendRequestName)
            val btnFrAccept = itemView.findViewById<Button>(R.id.btn_frAccept)
            val btnFrDecline = itemView.findViewById<Button>(R.id.btn_frDecline)

            context = super.itemView.context

            //displayed name is the name from the sending user
            name.text = request.from_name


            //accept button functionality
            btnFrAccept.setOnClickListener {

                //create Friendship in DataBase with information from sender and receiver of the FriendRequest
                //function created in FriendRequest Class unfortunately we only able to make barely any code clean like this
                FriendRequest(request.from_uid, request.to_uid).accept()

                //Go back to the friends list (Friend Activity)
                val intent = Intent(context, FriendActivity::class.java)
                startActivity(context, intent, null)

                //toast feedback to user
                Toast.makeText(context, "Friend request accepted from " + request.from_uid, Toast.LENGTH_SHORT).show()

            }

            //decline button functionality
            btnFrDecline.setOnClickListener {

                //delete Friend Request in DB
                FriendRequest(request.from_uid, request.to_uid).decline()

                //Go back to the friends list (Friend Activity)
                val intent = Intent(context, FriendActivity::class.java)
                startActivity(context, intent, null)

                //toast feedback to user
                Toast.makeText(context, "Friend request declined from " + request.from_uid, Toast.LENGTH_SHORT).show()

            }


        }


    }
        //necessary code below :)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_friendrequest, parent, false)

        return FriendRequestViewHolder(itemView)


    }


    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.bind(listData[position], position)


    }


    override fun getItemCount(): Int {

        return friendRequestList.size
    }


}