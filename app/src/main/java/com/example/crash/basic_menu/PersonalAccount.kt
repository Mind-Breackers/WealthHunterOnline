package com.example.crash.basic_menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.crash.R
import com.example.crash.basic_menu.train_game.Train_game
import com.example.crash.databinding.PersonalAccount2Binding
import com.example.crash.sigInUp.Server.User
import com.example.crash.sigInUp.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class PersonalAccount : AppCompatActivity() {
    private lateinit var bindingclass: PersonalAccount2Binding
    private val dataPlayModel:DataPlayMenu by viewModels()
    lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val userRef = database.getReference("user")
    var user=User("Error",)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingclass = PersonalAccount2Binding.inflate(layoutInflater)
        setContentView(bindingclass.root)
        if(intent.getParcelableExtra<User>("User")!=null) {
            user = intent.getParcelableExtra<User>("User")!!
            readData(user)
        }else {
            var user = User("Error",)
            readData(user)
        }
    }

    override fun onStart() {
        super.onStart()
        dataPlayModel.activeMenu.observe(this,{
            if(it) {
                //изменить надо наверное будет
                dataPlayModel.imId.value = R.drawable.boy1
                dataPlayModel.nameP.value = "New Player"
                bindingclass.viewPager2.visibility = View.VISIBLE
                bindingclass.viewPager2.adapter =
                    ViewPagerFragmentStateAdapter(supportFragmentManager, this.lifecycle)
            }else{
                finish()
            }
        })
    }

    private fun openFrag(f: Fragment,idHolder:Int){
        supportFragmentManager.beginTransaction().replace(idHolder,f).commit()
    }


    fun readData(usertemp: User){
        user=usertemp
        val avatar = R.drawable.boy1
        val login = user.login

        if(login=="admin"){
            openFrag(Train_game.newInstance(),bindingclass.menuHolder.id)

            bindingclass.viewPager2.visibility= View.GONE
        }
        else {
            bindingclass.viewPager2.adapter = ViewPagerFragmentStateAdapter(supportFragmentManager, this.lifecycle)
        }

        dataPlayModel.imId.value=avatar
        dataPlayModel.nameP.value=login
    }


}

