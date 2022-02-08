package com.example.todoapp

data class FriendRequest(
    var from_user : String,
    var to_user : String,
    var accepted : Boolean ?= false
) {
    fun accept(){
        //toDo 1. create object friendship
        //todo 2. friendship.sendToDb()
        //todo 3. self.delete
    }

    fun decline() {
        //todo self.delete
    }

    fun delete(){
       //todo if (status = true){ databaseReference.child(from_uid + to_uid)removeValue()
     }


}

