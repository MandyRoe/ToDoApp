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


class FriendRequestsActivity : AppCompatActivity()  {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var friendRequestRecyclerView: RecyclerView
    private lateinit var friendRequestArrayList: ArrayList<FriendRequest>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friendrequest)

        auth = FirebaseAuth.getInstance()
        friendRequestRecyclerView = findViewById(R.id.rvFriendRequestItems)
        friendRequestRecyclerView.layoutManager = LinearLayoutManager(this)
        friendRequestRecyclerView.setHasFixedSize(true)
        friendRequestArrayList = arrayListOf<FriendRequest>()

        val uid = auth.currentUser?.uid


        readFriendRequests(uid!!)


    }



    private fun readFriendRequests(uid: String) {

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("FriendRequests")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {   //addValueEventListener loops infinite

            override fun onDataChange(snapshot: DataSnapshot) {

                for (frSnapshot in snapshot.children) {

                    if (frSnapshot.child("to_uid").getValue()!!.equals(uid)) {             //ToDo: filter Users by friend status



                        val request = frSnapshot.getValue(FriendRequest::class.java)
                            friendRequestArrayList.add(request!!)                              //arrayList with all the users in Database
                            friendRequestRecyclerView.adapter = FriendRequestAdapter(friendRequestArrayList)


                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


}



























