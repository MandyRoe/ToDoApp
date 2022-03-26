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
 * Activity to select a friend when sharing a todo
 **/

class SelectUsersActivity : AppCompatActivity()  {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference2 : DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var shareUserArrayList: ArrayList<User>

    //prepare necessary variables
    var shareTitle : String ?= null
    var shareDescr : String ?= null
    var shareDueDate : String  = "null"
    var shareCreatedDate : String = "null"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectusers)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
        userRecyclerView = findViewById(R.id.rvUserItems)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        shareUserArrayList = arrayListOf<User>()

        //receive passed on values from ToDo Adapter
        val uid = auth.currentUser?.uid
        val title = intent.getStringExtra("title").toString()
        val description = intent.getStringExtra("description").toString()
        val dueDate = intent.getStringExtra("dueDate").toString()
        val createdDate= intent.getStringExtra("createdDate").toString()

        //basically copy passed on values
        shareTitle = title
        shareDescr = description
        shareDueDate = dueDate
        shareCreatedDate = createdDate

        readFriends(uid!!)


    }


    //show your friends function
    private fun readFriends(uid: String) {

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Friendships")
        databaseReference2= FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //loop through Friendship items in database
                for (friendSnapshot in snapshot.children) {
                    //look for my uid in friendships
                    if(friendSnapshot.key?.contains(uid)!!) {
                        //if uid1 = myself -> uid2 added to array
                        if(friendSnapshot.child("uid1").toString() == uid){

                            databaseReference2.addListenerForSingleValueEvent(object: ValueEventListener {

                                override fun onDataChange(snapshot: DataSnapshot)  {
                                    //loop through users to get user from uid2
                                    for (userSnapshot in snapshot.children) {

                                        if(friendSnapshot.child("uid2").getValue() == userSnapshot.key) {
                                            val user = userSnapshot.getValue(User::class.java)
                                            //add user to array for adapter to display
                                            shareUserArrayList.add(user!!)
                                            userRecyclerView.adapter = UserAdapter(shareTitle!!, shareDescr!!, shareDueDate!!, shareCreatedDate!! ,shareUserArrayList)


                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })

                        }
                        //if uid2 = myself -> uid1 added to array
                        else { databaseReference2.addListenerForSingleValueEvent(object: ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot)  {
                                //loop through users in db
                                for (userSnapshot in snapshot.children) {
                                    //get user from uid1
                                    if(friendSnapshot.child("uid1").getValue() == userSnapshot.key) {
                                        val user = userSnapshot.getValue(User::class.java)
                                        //add to array to display
                                        shareUserArrayList.add(user!!)
                                        userRecyclerView.adapter = UserAdapter(shareTitle!!, shareDescr!!, shareDueDate!!, shareCreatedDate!! ,shareUserArrayList)


                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })





                        }


                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


}



























