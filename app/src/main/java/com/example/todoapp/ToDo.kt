package com.example.todoapp

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * class for ToDo object
 **/

data class ToDo(
    val title: String ?= null,
    val description: String ?= null,
    val uid: String ?= null,
    var done: Boolean ?= false,
    var doneDate :String = "null",
    var dueDate : String  = "null",
    var createdDate: String = "null"
)
