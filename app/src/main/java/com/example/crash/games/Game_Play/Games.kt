package com.example.crash.games.Game_Play


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.example.crash.R
import com.example.crash.basic_menu.DataPlayMenu
import com.example.crash.databinding.Games2Binding
import com.example.crash.games.ClassForGame.*
import com.example.crash.games.Game_Play.Baggage.BaggageFragment
import com.example.crash.sigInUp.Server.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Games : AppCompatActivity() {
    lateinit var bindingclass: Games2Binding
    lateinit var root: RelativeLayout
    private val dataPlayMenu: DataPlayMenu by viewModels()
    lateinit var FieldPlayer1: Field
    private val  dataGames:DataGames by viewModels()
    val blocks = ArrayList<Block>()
    var downX = 0
    lateinit var userbottom:User
    lateinit var userTop:User
    val database = Firebase.database
    val ref=database.getReference("user")
    private var size = 0
    private var sizeBaggage = 0

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingclass = Games2Binding.inflate(layoutInflater)
        setContentView(bindingclass.root)

        val displaymetrics = resources.displayMetrics
        val displayheight = displaymetrics.heightPixels
        val displaywidth = displaymetrics.widthPixels

        size = intent.getIntExtra("difficult", 0) * 10
        userbottom =intent.getParcelableExtra<User>("Bottom")!!
        userTop =intent.getParcelableExtra<User>("Top")!!
       bindingclass.logTop.text=userTop.login
       bindingclass.logBottom.text=userbottom.login


        val field1Centerheight = displayheight / 2 + displayheight / 3
        val field1Centerwidth = displaywidth / 2
        val field2Centerheight = displayheight / 4
        val field2Centerwidth = displaywidth / 2




        bindingclass.Player1.post {
            val game = GameClass(
                bindingclass.rlGames,
                displayheight,
                displaywidth,
                bindingclass.rlPool,
                bindingclass.Player1,
                bindingclass.Player2,
                bindingclass.baggageBtn,
                dataGames,
                size,
                bindingclass.musorkaSvg2,
                musorkaEnemy= bindingclass.musorkaSvg1,
                baggageBtnEnemy=bindingclass.baggageBtnEnemy,
                userbottom = userbottom,
                userTop = userTop
            )


            val paramsbtn = bindingclass.baggageBtn.layoutParams as RelativeLayout.LayoutParams
            paramsbtn.leftMargin = (displaywidth - (displaywidth - game.fieldwidth) / 2) + (displaywidth - game.fieldwidth) / 16
            paramsbtn.topMargin = displayheight - bindingclass.baggageBtn.height
            bindingclass.baggageBtn.layoutParams = paramsbtn

            val paramsbtnEnemy = bindingclass.baggageBtnEnemy.layoutParams as RelativeLayout.LayoutParams
            paramsbtnEnemy.leftMargin = (displaywidth - (displaywidth - game.fieldwidth) / 2) + (displaywidth - game.fieldwidth) / 16
            bindingclass.baggageBtnEnemy.layoutParams = paramsbtnEnemy

            val paramsMusorka = bindingclass.musorkaSvg2.layoutParams as RelativeLayout.LayoutParams
            paramsMusorka.rightMargin = (displaywidth - (displaywidth - game.fieldwidth) / 2) + (displaywidth - game.fieldwidth) / 16
            paramsMusorka.leftMargin = (displaywidth - game.fieldwidth) / 16
            paramsMusorka.topMargin = displayheight - bindingclass.baggageBtn.height
            bindingclass.musorkaSvg2.layoutParams = paramsMusorka

            val paramsMusorkaEnemy = bindingclass.musorkaSvg1.layoutParams as RelativeLayout.LayoutParams
            paramsMusorkaEnemy.leftMargin=(displaywidth - game.fieldwidth) / 16
            bindingclass.musorkaSvg1.layoutParams=paramsMusorkaEnemy

            val paramslnBtn = bindingclass.lnBaggage.layoutParams as LinearLayout.LayoutParams
            paramslnBtn.height = bindingclass.baggageBtn.height
            bindingclass.lnBaggage.layoutParams = paramslnBtn
            bindingclass.lnBaggage3.layoutParams = paramslnBtn
            bindingclass.lnBaggage1.layoutParams = paramslnBtn

            val paramslnbaggage = bindingclass.baggageEnemy.layoutParams as LinearLayout.LayoutParams
            paramslnbaggage.height = game.blocksPool.height/2
            bindingclass.baggageEnemy.layoutParams = paramslnbaggage


                val params = bindingclass.skillsLeftPlayer1.layoutParams as LinearLayout.LayoutParams
                params.width = (displaywidth - game.fieldwidth) / 2
                params.height = displayheight - (game.blocksPool.posY + game.blocksPool.height)-50
                bindingclass.skillsLeftPlayer1.layoutParams = params

                val params1 = bindingclass.skillsRight.layoutParams as LinearLayout.LayoutParams
                params1.width = (displaywidth - game.fieldwidth) / 2
                params1.height = displayheight - (game.blocksPool.posY + game.blocksPool.height) - bindingclass.baggageBtn.height-50
                bindingclass.skillsRight.layoutParams = params1



            val params2 = bindingclass.field.layoutParams as LinearLayout.LayoutParams
            params2.width = game.fieldwidth
            params2.height = game.fieldheight
            bindingclass.field.layoutParams = params2





            val tree =SkillsTree(bindingclass.rlGames,bindingclass.tree,game,game.field1Centerheight)
            val treeEnemy =SkillsTree(bindingclass.rlGames,bindingclass.treeEnemy,game,game.field2Centerheight)


            //Туман
            val tuman=SkillsTuman(bindingclass.tuman,game,game.enemyField,false)
            game.blocksPool.tumanRerollOut.observe(this) {
                tuman.flagSkiils = true
                game.blocksPool.tumanRerollIn = true
                game.enemyField.visible(true)
            }

            val tumanenemy=SkillsTuman(bindingclass.tumanenemy,game,game.playField,true)
            game.blocksPool.tumanRerollOutenemy.observe(this) {
                tumanenemy.flagSkiils = true
                game.blocksPool.tumanRerollInenemy = true
                game.playField.visible(true)
            }
            ///-----------------------------------

            //Вода
            val water=SkillsWater(bindingclass.water,game)
            game.blocksPool.waterRerollOut.observe(this) {
                water.action = 0
            }
            //----------------------------------

            val fire=SkillsFire(bindingclass.fire,bindingclass.deleteRelative,game,true,displayheight,displaywidth)
            val fire2=SkillsFire(bindingclass.fireEnemy,bindingclass.deleteRelative1,game,false,displayheight,displaywidth)

            dataGames.BlockOutBaggage.observe(this) {
                if(game.baggaeBlock.isNotEmpty()) {
                    game.baggaeBlock.removeLast()
                    val bl =
                        Block(bindingclass.rlPool, field1Centerwidth, field1Centerheight, 50, it)
                    game.blocks.add(bl)
                }
            }

            dataGames.BlockOutBaggageEnemy.observe(this) {
                if(game.baggaeBlockEnemy.isNotEmpty()) {
                    game.baggaeBlockEnemy.removeLast()
                    val bl = Block(bindingclass.rlPool, field2Centerwidth, field2Centerheight/2, 50, it)
                    game.blocks.add(bl)
                }
            }

            bindingclass.baggageBtn.setOnClickListener {
                game.baggaeBlock.clear()
                game.baggaeBlock.addAll(game.newblockInBaggage)
                game.newblockInBaggage.clear()
                if (dataGames.BlockInBaggage1.value != null) {
                    game.baggaeBlock.addAll(dataGames.BlockInBaggage1.value!!)
                    if (dataGames.BlockInBaggage2.value != null) {
                        game.baggaeBlock.addAll(dataGames.BlockInBaggage2.value!!)
                    }
                }
                if (game.baggaeBlock.size < 7) {
                    if (game.baggaeBlock.size - 3 > 0) {
                        var i = 0
                        val baggage1 = arrayListOf<Block>()
                        game.baggaeBlock.forEach {
                            if (i < 3) {
                                baggage1.add(it)
                                i++
                            }
                        }
                        val baggage2 = arrayListOf<Block>()
                        for (index in 3 until game.baggaeBlock.size) {
                            baggage2.add(game.baggaeBlock[index])
                        }
                        dataGames.BlockInBaggage1.value = baggage1
                        dataGames.BlockInBaggage2.value = baggage2
                    } else {
                        val baggage3 = arrayListOf<Block>()
                        baggage3.addAll(game.baggaeBlock)
                        dataGames.BlockInBaggage1.value = baggage3
                        dataGames.BlockInBaggage2.value = null
                    }
                }
                dataGames.TopOrBottom.value=true
                openFrag(BaggageFragment.newInstance(), bindingclass.baggage.id)
                bindingclass.baggage.visibility = View.VISIBLE
                it.isEnabled = false
            }


            bindingclass.baggageBtnEnemy.setOnClickListener {
                game.baggaeBlockEnemy.clear()
                game.baggaeBlockEnemy.addAll(game.newblockInBaggageEnemy)
                game.newblockInBaggageEnemy.clear()
                if (dataGames.BlockInBaggageEnemy1.value != null) {
                    game.baggaeBlockEnemy.addAll(dataGames.BlockInBaggageEnemy1.value!!)
                    if (dataGames.BlockInBaggageEnemy2.value != null) {
                        game.baggaeBlockEnemy.addAll(dataGames.BlockInBaggageEnemy2.value!!)
                    }
                }
                if (game.baggaeBlockEnemy.size < 7) {
                    if (game.baggaeBlockEnemy.size - 3 > 0) {
                        var i = 0
                        val baggage1 = arrayListOf<Block>()
                        game.baggaeBlockEnemy.forEach {
                            if (i < 3) {
                                baggage1.add(it)
                                i++
                            }
                        }
                        val baggage2 = arrayListOf<Block>()
                        for (index in 3 until game.baggaeBlockEnemy.size) {
                            baggage2.add(game.baggaeBlockEnemy[index])
                        }
                        dataGames.BlockInBaggageEnemy1.value = baggage1
                        dataGames.BlockInBaggageEnemy2.value = baggage2
                    } else {
                        val baggage3 = arrayListOf<Block>()
                        baggage3.addAll(game.baggaeBlockEnemy)
                        dataGames.BlockInBaggageEnemy1.value = baggage3
                        dataGames.BlockInBaggageEnemy2.value = null
                    }
                }
                dataGames.TopOrBottom.value=false
                openFrag(BaggageFragment.newInstance(), bindingclass.baggageEnemy.id)
                bindingclass.baggageEnemy.visibility = View.VISIBLE
                it.isEnabled = false
            }



            sizeBaggage = game.blocksPool.height / 2
            bindingclass.Player1.post {
                bindingclass.baggage.layoutParams.height = sizeBaggage
            }

            game.winAction.observe(this){
                if(it){
                    bindingclass.winLog.text=userbottom.login
                    bindingclass.winSplash.visibility=View.VISIBLE
                    bindingclass.btnGame.setOnClickListener {
                        plusRaiting(userbottom)
                    }
                }else{
                    bindingclass.winLog.text=userTop.login
                    bindingclass.winSplash.visibility=View.VISIBLE
                    bindingclass.btnGame.setOnClickListener {
                        plusRaiting(userTop)
                    }
                }
            }

            game.musorkaSkiils.observe(this){
                when(it){
                    Block.Type.EARTH->{
                        tree.blockForactive++
                        if(tree.blockForactive==2) {
                            bindingclass.tree.clearColorFilter()
                            bindingclass.tree.isEnabled=true
                        }
                    }
                    Block.Type.FIRE->{
                        fire.blockForactive++
                        if(fire.blockForactive==2) {
                            bindingclass.fire.clearColorFilter()
                            bindingclass.fire.isEnabled=true
                        }
                    }
                    Block.Type.AIR->{
                        tuman.blockForactive++
                        if(tuman.blockForactive==2){
                            bindingclass.tuman.clearColorFilter()
                            bindingclass.tuman.isEnabled=true
                        }
                    }
                    Block.Type.WATER->{

                    }
                    else->{

                    }
                }
            }
            bindingclass.tuman.isEnabled=false
            bindingclass.fire.isEnabled=false
            bindingclass.tree.isEnabled=false
            bindingclass.tumanenemy.isEnabled=false
            bindingclass.fireEnemy.isEnabled=false
            bindingclass.treeEnemy.isEnabled=false
            game.musorkaSkiilsenemy.observe(this){
                when(it){
                    Block.Type.EARTH->{
                        treeEnemy.blockForactive++
                        if(treeEnemy.blockForactive==2) {
                            bindingclass.treeEnemy.clearColorFilter()
                            bindingclass.treeEnemy.isEnabled=true
                        }
                    }
                    Block.Type.FIRE->{
                        fire2.blockForactive++
                        if(fire2.blockForactive==2) {
                            bindingclass.fireEnemy.clearColorFilter()
                            bindingclass.fireEnemy.isEnabled=true
                        }
                    }
                    Block.Type.AIR->{
                        tumanenemy.blockForactive++
                        if(tumanenemy.blockForactive==2){
                            bindingclass.tumanenemy.clearColorFilter()
                            bindingclass.tumanenemy.isEnabled=true
                        }
                    }
                    Block.Type.WATER->{

                    }
                    else->{

                    }
                }
            }
        }

        dataGames.LifeBaggage.observe(this) {
            bindingclass.baggage.visibility = View.GONE
            bindingclass.baggageBtn.isEnabled = true
        }

        dataGames.LifeBaggageEnemy.observe(this) {
            bindingclass.baggageEnemy.visibility = View.GONE
            bindingclass.baggageBtnEnemy.isEnabled = true
        }




    }


    override fun onDestroy() {
        dataPlayMenu.activeMenu.value=true
        super.onDestroy()

        dataGames.LifeBaggage.removeObservers(this)
        dataGames.BlockOutBaggage.removeObservers(this)
    }

    private fun openFrag(f: Fragment, idHolder: Int) {
        if (dataGames.TopOrBottom.value == true) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out).add(idHolder, f).commit()
        }else{
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enemy_slide_in, R.anim.slide_out).add(idHolder, f).commit()
        }
        if (f.isVisible()) {
            supportFragmentManager.beginTransaction().replace(idHolder, f).commit()
        } else {

        }

    }

    fun plusRaiting(user: User){
        if(user.uid!=null ) {
            ref.child(user.uid!!).setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                }
            }
        }else{
        }
    }
}



