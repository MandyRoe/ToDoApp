package com.example.todoapp

import com.google.firebase.database.*


class User(
    var uid : String ?= null,
    var firstName : String ?= null,
    var lastName : String ?= null,
    var bio : String ?= null,

)