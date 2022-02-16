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



class AddFriendAdapter constructor(private val addFriendUserList : ArrayList<User>) : RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var context : Context
    private var listData: MutableList<User> = addFriendUserList as MutableList<User>



    inner class AddFriendViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.tvAddFriendName)
        //fetched info


        fun bind(user: User, index: Int){

            val btnAddFriendOnItem = itemView.findViewById<Button>(R.id.btnAddFriendOnItem)

            context = super.itemView.context
            name.text = user.firstName.toString() + " " + user.lastName.toString()



            btnAddFriendOnItem.setOnClickListener{

                databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("FriendRequests")
                val dbr2 : DatabaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
                auth = FirebaseAuth.getInstance()
                val fromUid = auth.currentUser?.uid.toString()
                val toUid = user.uid.toString()
                val toName = user.firstName + " " + user.lastName


                dbr2.addListenerForSingleValueEvent(object: ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (users in snapshot.children) {

                            if (users.child("uid").getValue()!!.equals(fromUid)) {             //ToDo: filter Users by friend status

                                val fn = users.child("firstName").getValue().toString()
                                val ln = users.child("lastName").getValue().toString()
                                val fromName = fn + " " + ln

                                val frRequest = FriendRequest(fromUid, toUid, fromName, toName, false)

                                databaseReference.child(fromUid +toUid).setValue(frRequest)


                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })


                val intent = Intent(context, DashboardActivity::class.java)

                startActivity(context, intent, null)
                Toast.makeText(context, "Friend request sent to " + toName, Toast.LENGTH_SHORT).show()

                println("clicked")


            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend_add, parent, false)

        return AddFriendViewHolder(itemView)



    }


    override fun onBindViewHolder(holder: AddFriendViewHolder, position: Int) {
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

        return addFriendUserList.size
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