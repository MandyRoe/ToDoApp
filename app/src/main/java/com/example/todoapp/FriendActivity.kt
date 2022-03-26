package com.example.todoapp
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.authentification.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlin.collections.ArrayList

/**
 * Activity to show user items that have friend status (friendslist)
 **/


class FriendActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference2 : DatabaseReference
    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var friendsArrayList: ArrayList<User>

    private lateinit var toggle: ActionBarDrawerToggle


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        auth = FirebaseAuth.getInstance()
        friendRecyclerView = findViewById(R.id.rvFriendItems)
        friendRecyclerView.layoutManager = LinearLayoutManager(this)
        friendRecyclerView.setHasFixedSize(true)
        friendsArrayList = arrayListOf<User>()

        val uid = auth.currentUser?.uid


        readFriends(uid!!)

        //add friend button functionality
        val btnAddFriend= findViewById<Button>(R.id.btnAddFriend)
        btnAddFriend.setOnClickListener {

            //go to AddFriendActivity that lets you add friends
            startActivity(Intent(this, AddFriendActivity::class.java))


        }

        //friend requests button functionality
        val btnFriendRequests= findViewById<Button>(R.id.btnFriendRequests)
        btnFriendRequests.setOnClickListener {
            println("button pressed successfully")

            //go to FriendRequestActivity to see your received friend requests
            startActivity(Intent(this, FriendRequestsActivity::class.java))


        }




        //toggle home button functionality
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //nav items functionality - if item is clicked start corresponding activity
        nav_view.bringToFront()
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))

                R.id.nav_logout -> {
                    auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))

                R.id.nav_friends -> startActivity(Intent(this, FriendActivity::class.java))


                R.id.nav_calendar -> startActivity(
                    Intent(
                        this,
                        CalendarActivity::class.java
                    )
                )

                R.id.nav_analysis -> startActivity(Intent(this, AnalysisActivity::class.java))


            }
            true
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)

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
                                            friendsArrayList.add(user!!)
                                            friendRecyclerView.adapter = FriendAdapter(friendsArrayList)


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
                                        friendsArrayList.add(user!!)
                                        friendRecyclerView.adapter = FriendAdapter(friendsArrayList)


                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })





                        } //else end


                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }





    // hamburger button functionality
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //don't close app if drawer is open and back is pressed
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        return super.onBackPressed()
    }


}
