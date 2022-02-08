package com.example.todoapp

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Friendship(
        val uid1 :String,
        val uid2: String
) {
        fun sendToDb(){
                val databaseReference : DatabaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Friendships")
                val a = databaseReference.child(this.uid1 + this.uid2)
                a.child("uid1").setValue(uid1)
                a.child("uid2").setValue(uid2)
        }

}