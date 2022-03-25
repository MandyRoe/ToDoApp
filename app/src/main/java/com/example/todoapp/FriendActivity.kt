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


class FriendActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
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
        // val title = intent.getStringExtra("title").toString()


        readFriends(uid!!)


        val btnAddFriend= findViewById<Button>(R.id.btnAddFriend)
        btnAddFriend.setOnClickListener {
            println("add Friend pressed successfully")

            startActivity(Intent(this, AddFriendActivity::class.java))


        }

        val btnFriendRequests= findViewById<Button>(R.id.btnFriendRequests)
        btnFriendRequests.setOnClickListener {
            println("button pressed successfully")

            startActivity(Intent(this, FriendRequestsActivity::class.java))


        }




        //toggle home button functionality
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //nav items functionality
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



            }
            true
        }

    } //onCreate end

    //functions


    @RequiresApi(Build.VERSION_CODES.O)


    private fun readFriends(uid: String) {

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Friendships")
        val dbr2 : DatabaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (friendSnapshot in snapshot.children) {

                    if(friendSnapshot.key?.contains(uid)!!) {                //look for my uid in friendships
                        if(friendSnapshot.child("uid1").toString() == uid){            //    if uid1 = my dann uid2 ins array

                            dbr2.addListenerForSingleValueEvent(object: ValueEventListener {

                                override fun onDataChange(snapshot: DataSnapshot)  {

                                    for (userSnapshot in snapshot.children) {         //get user from uid2

                                        if(friendSnapshot.child("uid2").getValue() == userSnapshot.key) {
                                            val user = userSnapshot.getValue(User::class.java)

                                            friendsArrayList.add(user!!)
                                            friendRecyclerView.adapter = FriendAdapter(friendsArrayList)


                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })

                        } else { dbr2.addListenerForSingleValueEvent(object: ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot)  {

                                for (userSnapshot in snapshot.children) {         //get user from uid2

                                    if(friendSnapshot.child("uid1").getValue() == userSnapshot.key) {
                                        val user = userSnapshot.getValue(User::class.java)

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
