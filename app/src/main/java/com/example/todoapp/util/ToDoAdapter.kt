package com.example.todoapp


import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*

// contains the main logic of the app
class ToDoAdapter(
    private val todos: MutableList<ToDo>
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // information from where the layout comes from - which xml should be "inflate"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    // add the new to do at the end of the list
    fun addToDoDo(todo: ToDo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    // checks with the help of boolean if the item is done
    fun deleteDoneTodos() {
        todos.removeAll { todo ->
            todo.isCheck
        }
        notifyDataSetChanged()
    }

    // checked or unchecked items. based on binary (paintFlags)
    private fun toggleStrikeThrough(tvToDoTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    // bind the data from the to do list to the view of the list
    // set the new input to do into the check box
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val curToDo = todos[position]
        holder.itemView.apply {
            tvToDoTitle.text = curToDo.title
            cbDone.isChecked = curToDo.isCheck
            toggleStrikeThrough(tvToDoTitle, curToDo.isCheck)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(tvToDoTitle, isChecked)
                curToDo.isCheck = !curToDo.isCheck
            }
        }
    }

    // returns the amount of items in the list
    override fun getItemCount(): Int {
        return todos.size
    }
}