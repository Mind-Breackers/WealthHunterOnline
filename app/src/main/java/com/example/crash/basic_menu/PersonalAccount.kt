package com.example.crash.basic_menu

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.crash.R
import com.example.crash.basic_menu.train_game.Train_game
import com.example.crash.databinding.PersonalAccount2Binding
import com.example.crash.games.Game_Play.Games

import com.example.crash.sigInUp.Server.User
import com.example.crash.sigInUp.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class PersonalAccount : AppCompatActivity() {
    private lateinit var bindingclass: PersonalAccount2Binding
    private val dataPlayModel:DataPlayMenu by viewModels()
    lateinit var auth: FirebaseAuth
    lateinit var soundFon:MediaPlayer
    lateinit var soundFon2:MediaPlayer
    lateinit var soundFon3:MediaPlayer
    val database = Firebase.database
    lateinit var audioManager:AudioManager
    val userRef = database.getReference("user")
    var user=User("Error", uid = null )
    var userGuest=User("Error", uid = null )
    var flaguserGuest=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingclass = PersonalAccount2Binding.inflate(layoutInflater)
        setContentView(bindingclass.root)

    }



    override fun onStart() {
        super.onStart()

        if(intent.getParcelableExtra<User>("User")!=null) {
            user = intent.getParcelableExtra<User>("User")!!
            flaguserGuest=false
            user.uid?.let { raitinSearch(userRef, it,user) }
            readData(user)
        }
        if(intent.getParcelableExtra<User>("Guest")!=null) {
            userGuest = intent.getParcelableExtra<User>("Guest")!!
            flaguserGuest=true
            userGuest.uid?.let { raitinSearch(userRef, it,userGuest) }
            readData(userGuest)
        }


           audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
           soundFon = MediaPlayer.create(this, R.raw.fonovoya)
           soundFon2 = MediaPlayer.create(this, R.raw.fonovoya2)
           soundFon3 = MediaPlayer.create(this, R.raw.fonovoya3)
           soundFon.start()

           dataPlayModel.managerSound.value = audioManager

           dataPlayModel.activeMenu.observe(this) {
               if (it) {
                   bindingclass.menuHolder.visibility = View.INVISIBLE
                   bindingclass.viewPager2.visibility = View.VISIBLE
               } else {
                   finish()
               }
           }


           dataPlayModel.activeTrain.observe(this) {
               if (it) {
                   bindingclass.viewPager2.visibility = View.INVISIBLE
                   bindingclass.menuHolder.visibility = View.VISIBLE
                   openFrag(Train_game.newInstance(), bindingclass.menuHolder.id)
               } else {
                   bindingclass.menuHolder.visibility = View.INVISIBLE
                   bindingclass.viewPager2.visibility = View.VISIBLE
               }
           }

    }

    override fun onDestroy() {
        super.onDestroy()
        soundFon.stop()
    }

    override fun onPause() {
        super.onPause()
        soundFon.pause()
    }

    override fun onResume() {
        super.onResume()

        soundFon.start()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            dataPlayModel.SeekBar.value=-1
        }
        if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            dataPlayModel.SeekBar.value=1
        }
        if(keyCode==KeyEvent.KEYCODE_VOLUME_MUTE){
            dataPlayModel.SeekBar.value=0
        }
        return true
    }

    private fun openFrag(f: Fragment,idHolder:Int){
        supportFragmentManager.beginTransaction().replace(idHolder,f).commit()
    }


    fun readData(usertemp: User){
        user=usertemp
        val login = user.login
        bindingclass.viewPager2.adapter = ViewPagerFragmentStateAdapter(supportFragmentManager, this.lifecycle)
        if(!flaguserGuest) {
            dataPlayModel.nameP.value = user
        }else{
            dataPlayModel.nameGuest.value=user
        }
    }

    fun raitinSearch(ref: DatabaseReference, idToken: String,user: User) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(idToken).value !=null) {
                    val raiting=snapshot.child(idToken).child("rating").value as Long
                    user.rating =raiting.toInt()
                }else{

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

}

