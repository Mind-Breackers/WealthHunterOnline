package com.example.crash.sigInUp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.crash.basic_menu.DataPlayMenu
import com.example.crash.basic_menu.PersonalAccount
import com.example.crash.sigInUp.Server.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class SplashScreen : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val userRef = database.getReference("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("SigIn","id23")
        auth = Firebase.auth
        if (auth.currentUser != null) {
          verification(userRef,auth.uid.toString())
        }else {
            Log.d("SigIn","id23")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun goPersonalAcoount(user:User){
        val intent = Intent(this, PersonalAccount::class.java)
        intent.putExtra("User",user)
        startActivity(intent)
        finish()
    }


    private fun verification(ref: DatabaseReference, id:String){
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(id).value!=null) {
                    val login=snapshot.child(id).child("login").value  as String
                    val rating=snapshot.child(id).child("rating").value  as Long
                    Log.d("SigIn","id222222=$login")
                    val user= User(login,rating.toInt())
                    goPersonalAcoount(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}