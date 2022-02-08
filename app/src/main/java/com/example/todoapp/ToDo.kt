package com.example.todoapp

// data classes in kotlin just for holding data without any logic
data class ToDo(
    val title: String ?= null,
    val description: String ?= null,
    val uid: String ?= null,
    var check: Boolean ?= false
)