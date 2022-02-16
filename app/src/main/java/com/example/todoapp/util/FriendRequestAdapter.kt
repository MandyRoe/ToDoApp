package com.example.todoapp


import android.content.Context
import android.content.Intent
import android.graphics.Paint.FAKE_BOLD_TEXT_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Build
import android.system.Os.remove
// LayoutInflater converts our xml Layout into kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.protobuf.Value
import kotlinx.android.synthetic.main.item_friendrequest.view.*
import kotlinx.android.synthetic.main.item_todo.view.*
import kotlinx.android.synthetic.main.item_todo.view.cbDone
import kotlinx.android.synthetic.main.item_user.view.*



class FriendRequestAdapter constructor(private val friendRequestList : ArrayList<FriendRequest>) : RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var context : Context
    private var listData: MutableList<FriendRequest> = friendRequestList as MutableList<FriendRequest>



    inner class FriendRequestViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){


        fun bind(request: FriendRequest, index: Int){
            val name : TextView = itemView.findViewById(R.id.tvFriendRequestName)
            val btnFrAccept = itemView.findViewById<Button>(R.id.btn_frAccept)
            val btnFrDecline = itemView.findViewById<Button>(R.id.btn_frDecline)

            context = super.itemView.context
            name.text = request.to_name



            btnFrAccept.setOnClickListener{

                FriendRequest(request.from_uid, request.to_uid).accept()    //create Friendship in DB

                val intent = Intent(context, FriendActivity::class.java)
                startActivity(context, intent, null)
                Toast.makeText(context, "Friend request accepted from " + request.from_uid, Toast.LENGTH_SHORT).show()

            }

            btnFrDecline.setOnClickListener{

                FriendRequest(request.from_uid, request.to_uid).decline()           //delete Friend Request in DB

                val intent = Intent(context, FriendActivity::class.java)
                startActivity(context, intent, null)
                Toast.makeText(context, "Friend request declined from " + request.from_uid, Toast.LENGTH_SHORT).show()

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_friendrequest, parent, false)

        return FriendRequestViewHolder(itemView)



    }


    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
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

        return friendRequestList.size
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