package com.example.app3.basic_menu

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.app3.R
import com.example.app3.basic_menu.train_game.Train_game
import com.example.app3.databinding.PersonalAccount2Binding

class PersonalAccount : AppCompatActivity() {
    private lateinit var bindingclass: PersonalAccount2Binding
    private val dataPlayModel:DataPlayMenu by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingclass = PersonalAccount2Binding.inflate(layoutInflater)
        setContentView(bindingclass.root)
        val avatar = intent.getIntExtra("Img", 0)!!
        val name = intent.getStringExtra("Name")!!
       if(name=="rmpty"){
            openFrag(Train_game.newInstance(),bindingclass.menuHolder.id)
            bindingclass.viewPager2.visibility= View.GONE
        }
        else {
            bindingclass.viewPager2.adapter = ViewPagerFragmentStateAdapter(supportFragmentManager, this.lifecycle)
        }

        dataPlayModel.imId.value=avatar
        dataPlayModel.nameP.value=name
    }

    override fun onStart() {
        super.onStart()
        dataPlayModel.activeMenu.observe(this,{
            dataPlayModel.imId.value=R.drawable.boy1
            dataPlayModel.nameP.value="New Player"
            bindingclass.viewPager2.visibility= View.VISIBLE
            bindingclass.viewPager2.adapter = ViewPagerFragmentStateAdapter(supportFragmentManager, this.lifecycle)
        })
    }

    private fun openFrag(f: Fragment,idHolder:Int){
        supportFragmentManager.beginTransaction().replace(idHolder,f).commit()
    }


}

