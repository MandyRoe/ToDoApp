package com.example.todoapp

import java.time.LocalDate
import java.time.LocalDateTime


// data classes in kotlin just for holding data without any logic
data class ToDo(
    val title: String ?= null,
    val description: String ?= null,
    val uid: String ?= null,
   // var check: Boolean ?= false,
    var dueDate : String  = "null",
    var createdDate: String = "null"
)