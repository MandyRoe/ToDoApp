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

                                    if(ds.child("uid1").getValue().toString() != users?.uid && ds.child("uid2").getValue().toString() != users?.uid) {  //check for friendship

                                        addFriendArrayList.add(users!!)                              //arrayList with all the users in Database
                                        friendRecyclerView.adapter = AddFriendAdapter(addFriendArrayList)

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
























