package com.example.crash.basic_menu

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.crash.R
import com.example.crash.basic_menu.train_game.Train_game
import com.example.crash.databinding.PersonalAccount2Binding
import com.example.crash.sigInUp.Server.User
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
    var user=User("Error")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingclass = PersonalAccount2Binding.inflate(layoutInflater)
        setContentView(bindingclass.root)
        if(intent.getParcelableExtra<User>("User")!=null) {
            user = intent.getParcelableExtra<User>("User")!!
            readData(user)
        }else {
            var user = User("Error")
            readData(user)
        }

    }



    override fun onStart() {
        super.onStart()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        soundFon=MediaPlayer.create(this,R.raw.fonovoya)
        soundFon2=MediaPlayer.create(this,R.raw.fonovoya2)
        soundFon3=MediaPlayer.create(this,R.raw.fonovoya3)
            soundFon.start()

        dataPlayModel.managerSound.value=audioManager
        dataPlayModel.activeMenu.observe(this,{
            if(it) {
                bindingclass.menuHolder.visibility=View.INVISIBLE
                bindingclass.viewPager2.visibility = View.VISIBLE
                bindingclass.viewPager2.adapter = ViewPagerFragmentStateAdapter(supportFragmentManager, this.lifecycle)
            }else{
                finish()
            }
        })
        dataPlayModel.activeTrain.observe(this,{
            if(it){
                bindingclass.viewPager2.visibility = View.INVISIBLE
                bindingclass.menuHolder.visibility=View.VISIBLE
                openFrag(Train_game.newInstance(),bindingclass.menuHolder.id)
            }
        })
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

