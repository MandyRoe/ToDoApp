package com.example.todoapp

// data classes in kotlin just for holding data without any logic
data class ToDo(
    val title: String,
    var isCheck: Boolean = false
)