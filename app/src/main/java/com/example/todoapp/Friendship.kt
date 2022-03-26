package com.example.todoapp

import com.google.firebase.database.*

/**
 * class for Friendship object
 * unfortunately we weren't able to implement everything so nicely. dirty was faster
 **/



class Friendship(
        private val uid1 :String,
        private val uid2: String
) {
        val databaseReference : DatabaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Friendships")


        fun createFriendship(){
                val fsPath = databaseReference.child(this.uid1 + this.uid2)
                fsPath.child("uid1").setValue(uid1)
                fsPath.child("uid2").setValue(uid2)
        }

}
