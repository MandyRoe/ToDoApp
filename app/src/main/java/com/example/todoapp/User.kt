package com.example.todoapp

data class User(
    var uid : String ?= null,
    var firstName : String ?= null,
    var lastName : String ?= null,
    var bio : String ?= null,
    var check: Boolean ?= false
)
