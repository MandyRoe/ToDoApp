package com.example.todoapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference


class ProfileActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")

        showProfile(uid!!)





        val createProfileButton = findViewById<Button>(R.id.btnCreateProfile)
        createOrEdit(createProfileButton)
        createProfileButton.setOnClickListener {

            val firstName = findViewById<EditText>(R.id.etFirstName)
            val lastName = findViewById<EditText>(R.id.etLastName)
            val bio = findViewById<EditText>(R.id.etBio)

            val firstNameText = firstName.text.toString()
            val lastNameText = lastName.text.toString()
            val bioText = bio.text.toString()


            val user = User(uid, firstNameText, lastNameText, bioText)
            if(uid != null){
                databaseReference.child(uid).setValue(user).addOnCompleteListener {

                    if (it.isSuccessful){
                        uploadProfilePic()
                        Toast.makeText(this@ProfileActivity, "Profile edit successful", Toast.LENGTH_SHORT).show()
                        refreshView()

                    }else{

                        Toast.makeText(this@ProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }

                }
            }


        }
        val homeButton = findViewById<Button>(R.id.homeBtn)
        toBeOrNotToBe(homeButton)
        homeButton.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }




    }
    //checks if Profile information is submitted to create profile

    private fun refreshView(){
        startActivity(Intent(this, ProfileActivity::class.java))
    }


    //load existing text for user from db
    private fun showProfile(uid : String) {
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {


                    if(ds.key!!.contains(uid)){
                        var fn = ds.child("firstName").getValue().toString()
                        var ln = ds.child("lastName").getValue().toString()
                        var b = ds.child("bio").getValue().toString()

                        if(fn == "null" && ln == "null"){

                            fn = ""
                            ln = ""
                            b = ""

                        }


                        val firstName = findViewById<EditText>(R.id.etFirstName)
                        firstName.setText(fn, TextView.BufferType.EDITABLE)

                        val lastName = findViewById<EditText>(R.id.etLastName)
                        lastName.setText(ln, TextView.BufferType.EDITABLE)

                        val bio = findViewById<EditText>(R.id.etBio)
                        bio.setText(b, TextView.BufferType.EDITABLE)

                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }

    //check if profile exists for create or change button
    private fun createOrEdit(button: Button) {
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {                      //todo registered check auf array umstellen
                for (ds in snapshot.getChildren()) {
                    if (ds.key == uid!!) {
                        var checkReg = ds.child("reg_flag").getValue()
                        if(checkReg !=true){
                            button?.text = "Edit Profile"
                            println("reg_flag doesn't exist so user is registered")
                        }
                        break
                    }
                }



            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }


    //home button shown?
    private fun toBeOrNotToBe(button: Button) {
        databaseReference = FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.getChildren()) {
                    var checkReg = ds.child("reg_flag").getValue()

                    if(checkReg ==true){
                        button.isEnabled = false
                        button.isVisible = false


                    } else if(checkReg == null){

                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }

    private fun uploadProfilePic() {

        imageUri = Uri.parse("android.resource://$packageName/${R.drawable.profile}")
        storageReference = FirebaseStorage.getInstance("gs://todoapp-ca2d3.appspot.com").getReference("Users/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri)

        println("picture uplaoded")


    }


}