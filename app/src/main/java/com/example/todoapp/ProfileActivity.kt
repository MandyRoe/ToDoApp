package com.example.todoapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_test.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import android.widget.EditText
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
        databaseReference =
            FirebaseDatabase.getInstance("https://todoapp-ca2d3-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Users")



        val saveButton = findViewById<Button>(R.id.btnSave)
        saveButton.setOnClickListener {
            println("2")

            val firstName = findViewById<EditText>(R.id.etFirstName)
            val lastName = findViewById<EditText>(R.id.etLastName)
            val bio = findViewById<EditText>(R.id.etBio)

            val firstNameText = firstName.text.toString()
            println(firstNameText)
            val lastNameText = lastName.text.toString()
            val bioText = bio.text.toString()


            val user = User(firstNameText, lastNameText, bioText)
            if(uid != null){
                println(uid)
                databaseReference.child(uid).setValue(user).addOnCompleteListener {

                    if (it.isSuccessful){
                        println("1")
                        uploadProfilePic()


                    }else{
                    println("notSuccessfull")

                        Toast.makeText(this@ProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }

                }
            }


        }
        val homeButton = findViewById<Button>(R.id.homeBtn)
        homeButton.setOnClickListener{
            val intent = Intent(this,DashboardActivity::class.java)
            startActivity(intent)
        }




    }
    private fun uploadProfilePic() {

        imageUri = Uri.parse("android.resource://$packageName/${R.drawable.profile}")
        storageReference = FirebaseStorage.getInstance("gs://todoapp-ca2d3.appspot.com").getReference("Users/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {

            Toast.makeText(this@ProfileActivity, "Profile successfully updated", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{
            Toast.makeText(this@ProfileActivity, "Failed to upload the image", Toast.LENGTH_SHORT).show()

        }


    }




}