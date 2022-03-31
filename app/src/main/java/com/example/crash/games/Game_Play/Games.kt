package com.example.crash.games.Game_Play


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.crash.R
import com.example.crash.databinding.Games2Binding
import com.example.crash.games.Block
import com.example.crash.games.Field
import com.example.crash.games.Game_Play.Baggage.BaggageFragment
import com.example.crash.games.Pool


class Games : AppCompatActivity() {
    lateinit var bindingclass: Games2Binding
    lateinit var root: RelativeLayout
    lateinit var FieldPlayer1: Field
    private val  dataGames:DataGames by viewModels()
    val blocks = ArrayList<Block>()
    var downX = 0
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
        root = bindingclass.rlPool
        size = intent.getIntExtra("difficult", 0) * 10 * 4

        val field1Centerheight = displayheight / 2 + displayheight / 3
        val field1Centerwidth = displaywidth / 2
        val blocksPool = Pool(root, blocks, 0, displayheight / 2 - displayheight / 5, displaywidth)
        val fieldheight = (displayheight - blocksPool.height) / 2 + 50
        val fieldwidth = displaywidth - (displaywidth / 6) * 2




        bindingclass.Player1.post {


            val paramsbtn = bindingclass.baggageBtn.layoutParams as RelativeLayout.LayoutParams
            paramsbtn.leftMargin =(displaywidth-(displaywidth - fieldwidth) / 2)+(displaywidth - fieldwidth) / 16
            paramsbtn.topMargin =displayheight-bindingclass.baggageBtn.height
            bindingclass.baggageBtn.layoutParams=paramsbtn

            val paramslnBtn=bindingclass.lnBaggage.layoutParams as  LinearLayout.LayoutParams
            paramslnBtn.height=bindingclass.baggageBtn.height
            bindingclass.lnBaggage.layoutParams=paramslnBtn


            val params = bindingclass.skillsLeftPlayer1.layoutParams as LinearLayout.LayoutParams
            params.width = (displaywidth - fieldwidth) / 2
            params.height = fieldheight
            bindingclass.skillsLeftPlayer1.layoutParams = params

            val params1 = bindingclass.skillsRight.layoutParams as LinearLayout.LayoutParams
            params1.width = (displaywidth - fieldwidth) / 2
            params1.height = fieldheight - bindingclass.baggageBtn.height
            bindingclass.skillsRight.layoutParams = params1


            val params2 = bindingclass.field.layoutParams as LinearLayout.LayoutParams
            params2.width = fieldwidth
            params2.height = fieldheight
            bindingclass.field.layoutParams = params2

            val game = GameClass(
                bindingclass.rlGames,
                displayheight,
                displaywidth,
                bindingclass.rlPool,
                bindingclass.Player1,
                bindingclass.Player2,
                bindingclass.baggageBtn,
                dataGames,
                size
            )

            dataGames.BlockOutBaggage.observe(this,{
                game.baggaeBlock.removeLast()
                val bl = Block(bindingclass.rlPool, field1Centerwidth, field1Centerheight, 50, it)
                game.blocks.add(bl)
            })

            bindingclass.baggageBtn.setOnClickListener {
                game.baggaeBlock.clear()
                game.baggaeBlock.addAll(game.newblockInBaggage)
                game.newblockInBaggage.clear()
                if(dataGames.BlockInBaggage1.value!=null) {
                    game.baggaeBlock.addAll(dataGames.BlockInBaggage1.value!!)
                    if (dataGames.BlockInBaggage2.value != null) {
                        game.baggaeBlock.addAll(dataGames.BlockInBaggage2.value!!)
                    }
                }
                if(game.baggaeBlock.size<7) {
                    if(game.baggaeBlock.size-3>0) {
                        var i=0
                        val baggage1= arrayListOf<Block>()
                        game.baggaeBlock.forEach { if(i<3){
                            baggage1.add(it)
                            i++
                        }
                        }
                        val baggage2=arrayListOf<Block>()
                        for (index in 3 until game.baggaeBlock.size){
                            baggage2.add(game.baggaeBlock[index])
                        }
                        dataGames.BlockInBaggage1.value =  baggage1
                        dataGames.BlockInBaggage2.value =  baggage2
                    }
                    else{
                        val baggage3= arrayListOf<Block>()
                        baggage3.addAll(game.baggaeBlock)
                        dataGames.BlockInBaggage1.value = baggage3
                        dataGames.BlockInBaggage2.value =  null
                    }
                }
                openFrag(BaggageFragment.newInstance(), bindingclass.baggage.id)
                bindingclass.baggage.visibility = View.VISIBLE
                it.isEnabled = false
            }
        }



        sizeBaggage = blocksPool.height / 2
        bindingclass.Player1.post {
            bindingclass.baggage.layoutParams.height = sizeBaggage
        }


        dataGames.LifeBaggage.observe(this, {
            bindingclass.baggage.visibility = View.GONE
            bindingclass.baggageBtn.isEnabled = true
        })

    }

    private fun openFrag(f: Fragment, idHolder: Int) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out).add(idHolder, f).commit()
        if (f.isVisible()) {
            supportFragmentManager.beginTransaction().replace(idHolder, f).commit()
        } else {

        }

    }
}



