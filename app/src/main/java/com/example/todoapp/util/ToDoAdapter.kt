package com.example.todoapp


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Build
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_todo.view.*
import java.time.LocalDateTime


class ToDoAdapter(private val dueDate : String, private val createdDate : String, private val todoList : ArrayList<ToDo>) : RecyclerView.Adapter<ToDoAdapter.TodoViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    var selectedTodo : String ?= null
    private var listData: MutableList<ToDo> = todoList as MutableList<ToDo>
    private lateinit var context : Context




    inner class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        //fetched info

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind (todo: ToDo, index: Int){
            databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
            val uid = auth.currentUser?.uid
            val title = itemView.findViewById<TextView>(R.id.tvTodoTitle)
            val description = itemView.findViewById<TextView>(R.id.tvDescription)

            val btn_delete = itemView.findViewById<Button>(R.id.btn_delete_todo)
            val btn_share = itemView.findViewById<Button>(R.id.btn_share_todo)





            context = super.itemView.context             //context for start activity intent
            title.text = todo.title
            description.text = todo.description





            btn_delete.setOnClickListener{
                deleteItem(index)
                databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
                databaseReference.child(title.text.toString() + uid.toString()).removeValue()

            }

            btn_share.setOnClickListener{

                //send data to other activity
                val intent = Intent(context, SelectUsersActivity::class.java).apply{
                    putExtra("title", title.text.toString() )
                    putExtra("description", description.text.toString())
                    putExtra("dueDate", dueDate)
                    putExtra("createdDate", createdDate)

                }
                startActivity(context, intent, null)


            }


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return todoList.size
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(listData[position], position)

         val currentItem = todoList[position]


         holder.itemView.apply {

             //show status of already existing toDos
             auth = FirebaseAuth.getInstance()
             var uid = auth.currentUser?.uid
             databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
             databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

                 override fun onDataChange(snapshot: DataSnapshot) {
                     for (ds in snapshot.children) {

                         if(ds.child("done").getValue() == true && ds.key == currentItem.title.toString() + uid ) {

                             item_whole.setBackgroundColor(Color.parseColor("#41c9c5"))

                         }
                         else if (ds.child("done").getValue() == false && ds.key == currentItem.title.toString() + uid ) {

                             item_whole.setBackgroundColor(Color.parseColor("#ffffff"))

                         }
                     }
                 }
                 override fun onCancelled(error: DatabaseError) {
                     //not needed
                 }


             })


            //update status on todos with click on check button
             btn_done_todo.setOnClickListener {

                 auth = FirebaseAuth.getInstance()
                 var uid = auth.currentUser?.uid
                 val doneDate = LocalDateTime.now().toString()
                 databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
                 databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

                     override fun onDataChange(snapshot: DataSnapshot) {
                         for (ds in snapshot.children) {

                                    println(ds.child("done"))
                                    if(ds.child("done").getValue() == false && ds.key == currentItem.title.toString() + uid ) {
                                        databaseReference.child(currentItem.title.toString() + uid).child("done").setValue(true)
                                        databaseReference.child(currentItem.title.toString() + uid).child("doneDate").setValue(doneDate)
                                        item_whole.setBackgroundColor(Color.parseColor("#41c9c5"))
                                        btn_done_todo.setBackgroundResource(R.drawable.undo_icon)

                                    }
                                     else if (ds.child("done").getValue() == true && ds.key == currentItem.title.toString() + uid ) {
                                      databaseReference.child(currentItem.title.toString() + uid).child("done").setValue(false)
                                       databaseReference.child(currentItem.title.toString() + uid).child("doneDate").setValue("null")
                                       item_whole.setBackgroundColor(Color.parseColor("#ffffff"))
                                        btn_done_todo.setBackgroundResource(R.drawable.check_icon)
                             }
                         }
                     }
                     override fun onCancelled(error: DatabaseError) {
                         //not needed
                     }


                 })




                 }
             }

    }

    fun deleteItem(index: Int){
        listData.removeAt(index)
        notifyDataSetChanged()
    }



}