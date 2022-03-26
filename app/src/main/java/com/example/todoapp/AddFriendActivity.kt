package com.example.todoapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.todoapp.UserAdapter.*
import kotlinx.android.synthetic.main.item_todo.*

/**
 * Activity to show users in db and request friendship
 **/


class AddFriendActivity : AppCompatActivity()  {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var addFriendArrayList: ArrayList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfriend)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
        friendRecyclerView = findViewById(R.id.rvFriendAddItems)
        friendRecyclerView.layoutManager = LinearLayoutManager(this)
        friendRecyclerView.setHasFixedSize(true)
        addFriendArrayList = arrayListOf<User>()

        val uid = auth.currentUser?.uid


        readUsers(uid!!)


    }


    //function for displaying all users but self
    //todo: only show users that are not self and not already friends
    private fun readUsers(uid: String) {

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //go through all users in db
                for (uS in snapshot.children) {

                    //if user id is not equal to user.key from db (which is uid of the respective user)
                if(uS.key != uid){
                    //Create user object from database values
                    val user = uS.getValue(User::class.java)
                    //add to User Array List and send to adapter
                    addFriendArrayList.add(user!!)
                    friendRecyclerView.adapter = AddFriendAdapter(addFriendArrayList)
                }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }



}
























