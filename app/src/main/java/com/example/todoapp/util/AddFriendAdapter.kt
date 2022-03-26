package com.example.todoapp


import android.content.Context
import android.content.Intent
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Adapter to create the recycler view for AddFriendActivity
 **/

//Adapter gets Array List of User items to display from AddFriendActivity
class AddFriendAdapter constructor(private val addFriendUserList: ArrayList<User>) : RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference2: DatabaseReference
    private lateinit var context: Context
    private var listData: MutableList<User> = addFriendUserList as MutableList<User>


    //View Holder for the recycler view
    inner class AddFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //link name to be displayed on the "friend_add" item
        val name: TextView = itemView.findViewById(R.id.tvAddFriendName)


        //bind values to current user item in array list (to know which item got interacted with in the recycler view and use the correct values)
        fun bind(user: User, index: Int) {
            context = super.itemView.context

            //initialize add friend button and link to "friend_add" item
            val btnAddFriendOnItem = itemView.findViewById<Button>(R.id.btnAddFriendOnItem)



            //initialize name to show first name and last name of respective user in text view field of the item
            name.text = user.firstName.toString() + " " + user.lastName.toString()


            //add friend button functionality
            btnAddFriendOnItem.setOnClickListener {

                //database references to assigned paths
                databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("FriendRequests")
                databaseReference2 = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
                //Initialize Firebase auth
                auth = FirebaseAuth.getInstance()

                //prepare different values
                val fromUid = auth.currentUser?.uid.toString()           //sender uid (current user)
                val toUid = user.uid.toString()                          //receiver uid
                val toName = user.firstName + " " + user.lastName        //receiver name


                databaseReference2.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        //go through each user in database
                        for (users in snapshot.children) {

                            //make sure user information selected from db loop is current user
                            if (users.key.equals(fromUid)) {

                                //assign values from db of current user
                                val fn = users.child("firstName").getValue().toString()
                                val ln = users.child("lastName").getValue().toString()
                                val fromName = fn + " " + ln

                                //create FriendRequest object with fetched values of current user
                                val frRequest = FriendRequest(fromUid, toUid, fromName, toName, false)
                                //post FriendRequest object to db
                                databaseReference.child(fromUid + toUid).setValue(frRequest)


                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                //go to MainActivity aswell after button click
                val intent = Intent(context, MainActivity::class.java)

                startActivity(context, intent, null)

                //toast feedback
                Toast.makeText(context, "Friend request sent to " + toName, Toast.LENGTH_SHORT).show()

            }


        }


    }
        //necessary functions below :)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend_add, parent, false)

        return AddFriendViewHolder(itemView)


    }


    override fun onBindViewHolder(holder: AddFriendViewHolder, position: Int) {
        holder.bind(listData[position], position)


    }

    override fun getItemCount(): Int {

        return addFriendUserList.size
    }


}