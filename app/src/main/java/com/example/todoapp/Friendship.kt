package com.example.todoapp

import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*


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
