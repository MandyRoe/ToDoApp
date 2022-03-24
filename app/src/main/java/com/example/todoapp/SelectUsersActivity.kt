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


class SelectUsersActivity : AppCompatActivity()  {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var shareUserArrayList: ArrayList<User>
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

        val uid = auth.currentUser?.uid
        val title = intent.getStringExtra("title").toString()
        val description = intent.getStringExtra("description").toString()
        val dueDate = intent.getStringExtra("dueDate").toString()
        val createdDate= intent.getStringExtra("createdDate").toString()



        shareTitle = title
        shareDescr = description
        shareDueDate = dueDate
        shareCreatedDate = createdDate

        readUsers(uid!!)


    }



    private fun readUsers(uid: String) {

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
        val dbr2 : DatabaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Friendships")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (uS in snapshot.children) {

                    if(uS.key != uid ) {                              //only show other users
                        val users = uS.getValue(User::class.java)
                        dbr2.addListenerForSingleValueEvent(object: ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot)  {
                                for (ds in snapshot.children) {

                                    if(ds.child("uid1").getValue().toString() == users?.uid || ds.child("uid2").getValue().toString() == users?.uid) {  //check for friendship
                                            shareUserArrayList.add(users!!)                              //arrayList with all the user owned todos in Database
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

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


}



























