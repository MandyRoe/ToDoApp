package com.example.todoapp


import android.content.Context
import android.content.Intent
import android.graphics.Paint.FAKE_BOLD_TEXT_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.protobuf.Value
import kotlinx.android.synthetic.main.item_todo.view.*
import kotlinx.android.synthetic.main.item_todo.view.cbDone
import kotlinx.android.synthetic.main.item_user.view.*



class UserAdapter constructor(private val title : String, private val descript:String, private val dueD : String, private val createdD : String,  private val userList : ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {



    private lateinit var databaseReference: DatabaseReference
    private lateinit var context : Context
    private var listData: MutableList<User> = userList as MutableList<User>



    inner class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.tvUserName)
        //fetched info


        fun bind(user: User, index: Int){


            val name = itemView.findViewById<TextView>(R.id.tvUserName)
            val btnShareToUser = itemView.findViewById<Button>(R.id.btnShareToUser)

            context = super.itemView.context
            name.text = user.firstName.toString() + " " + user.lastName.toString()



            btnShareToUser.setOnClickListener{

                databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")

                val uid = user.uid.toString()

                val todo = ToDo(title, descript, uid, dueD, createdD)

                databaseReference.child(title + uid).setValue(todo)
                val intent = Intent(context, ToDoActivity::class.java)

                startActivity(context, intent, null)
                Toast.makeText(context, "Todo Shared with " +user.firstName + " " + user.lastName, Toast.LENGTH_SHORT).show()

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)

        return UserViewHolder(itemView)



    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       holder.bind(listData[position], position)

        /* val currentItem = userList[position]
         holder.name.text = currentItem.firstName + " " +currentItem.lastName

         //fetched info


         holder.itemView.apply {

             cbDone.isChecked = currentItem.check!!
             toggleCheck(tvUserName, currentItem.check!!, currentItem.firstName!!, currentItem.lastName!!, currentItem.uid!! )  //check
             cbDone.setOnCheckedChangeListener { _, isChecked ->
                 toggleCheck(tvUserName, isChecked, currentItem.firstName!!, currentItem.lastName!!, currentItem.uid!!)   //uncheck
                 currentItem.check = !currentItem.check!!

             }
         }*/


    }


/*
    // checked or unchecked items. based on binary (paintFlags)
    private fun toggleCheck(tvUserName: TextView, isChecked: Boolean, firstName:String, lastName:String, uid:String) {
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")


        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (usersSnapshot in snapshot.children) {                             //go through all user ids

                    val fn = usersSnapshot.child("firstName").getValue().toString()                             //if name = name vom checked item
                    val ln = usersSnapshot.child("lastName").getValue().toString()


                    if (isChecked == true && fn ==firstName && ln == lastName) {       //set to true where uid matches current item of array that is checked
                        selectedUid = uid

                        println("********" +selectedUid + "ist die uid vom selected user")
                        tvUserName.paintFlags = tvUserName.paintFlags or FAKE_BOLD_TEXT_FLAG

                                // ToDo: change to uid check and not global check - setVale true+uid of current?? same with ToDo Adapter




                    } else {
                        tvUserName.paintFlags = tvUserName.paintFlags and FAKE_BOLD_TEXT_FLAG.inv()


                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }


        })
    }

*/

    override fun getItemCount(): Int {

        return userList.size
    }




        //was inside User Viewholder
        /*    auth = FirebaseAuth.getInstance()

            databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
            databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {   //addValueEventListener loops infinite

                override fun onDataChange(snapshot: DataSnapshot) {

                    for(ds in snapshot.getChildren()) {
                        var checkStatus : Boolean = ds.child("check").getValue() as Boolean

                        println(checkStatus)

                        if (checkStatus == true){
                            databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("ToDo")
                            val shareeUid : String = ds.key.toString()
                            val sharedTodoTitle : String = currentItem
                            val todo = ToDo(sharedTodoTitle, shareeUid, false)

                            databaseReference.child(sharedTodoTitle + shareeUid).setValue(todo)


                        }


                    }


                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        }

    }*/

    //fun getSelectedUid(users:Array<User>): String? {
        //return selectedUid
    //}


}