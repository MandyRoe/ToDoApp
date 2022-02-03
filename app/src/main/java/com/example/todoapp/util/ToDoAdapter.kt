package com.example.todoapp


import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*


class ToDoAdapter(private val todoList : ArrayList<ToDo>) : RecyclerView.Adapter<ToDoAdapter.todoViewHolder>() {






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): todoViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return todoViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: todoViewHolder, position: Int) {

        val currentitem = todoList[position]

        holder.title.text = currentitem.title
        //fetched info
    }



    override fun getItemCount(): Int {

        return todoList.size
    }





    class todoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val title : TextView = itemView.findViewById(R.id.tvToDoTitle)
        //fetched info

    }



}