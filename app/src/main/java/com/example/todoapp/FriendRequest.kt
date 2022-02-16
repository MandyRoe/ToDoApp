package com.example.todoapp

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class FriendRequest(
    var from_uid : String ="",
    var to_uid : String ="",
    var from_name : String ="",
    var to_name : String ="",
    var accepted : Boolean ?= false
) {


    fun accept(){
        Friendship(from_uid, to_uid).createFriendship()
        remove()

    }

    fun decline() {
        val databaseReference : DatabaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("FriendRequests")
        databaseReference.child(this.from_uid + this.to_uid).removeValue()
    }

    private fun remove(){
        val databaseReference : DatabaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("FriendRequests")
        databaseReference.child(this.from_uid + this.to_uid).removeValue()
     }


}

