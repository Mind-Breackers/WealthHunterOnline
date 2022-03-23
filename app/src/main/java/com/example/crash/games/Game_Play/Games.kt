package com.example.crash.games.Game_Play

import android.R.attr.button
import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.view.Window
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.example.crash.R
import com.example.crash.basic_menu.DataPlayMenu
import com.example.crash.databinding.Games2Binding
import com.example.crash.games.Block
import com.example.crash.games.Field
import com.example.crash.games.Pool


class Games : AppCompatActivity() {
    lateinit var bindingclass: Games2Binding
    lateinit var root: RelativeLayout
    lateinit var rootblock: RelativeLayout
    lateinit var FieldPlayer1: Field
    private val dataPlayMenu: DataPlayMenu by viewModels()
    val blocks = ArrayList<Block>()
    var capturedBlock: Block? = null
    var lastX = 0
    var lastY = 0
    var downX = 0
    var downY = 0
    private var size=0
    private var sizeBaggage=0

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

        val blocksPool = Pool(root, blocks, 0, displayheight / 2-displayheight/5, displaywidth)
        bindingclass.rlGames.post {
            val fieldheight = (displayheight-blocksPool.height)/2
            val fieldwidth = displaywidth-(displaywidth/6)*2
            val field1Centerheight = displayheight / 2 + displayheight / 3
            val field1Centerwidth = displaywidth / 2
            val field2Centerheight = displaywidth / 4
            val field2Centerwidth = displaywidth / 2


                FieldPlayer1 = Field(
                    bindingclass.Player1,
                    30, blocksPool.cellSize, field1Centerwidth, field1Centerheight, fieldheight,
                    fieldwidth
                )

                val FieldPlayer2 = Field(
                    bindingclass.Player2,
                    30, blocksPool.cellSize,field2Centerwidth, field2Centerheight,fieldheight,
                    fieldwidth
                )

            bindingclass.Player1.post {
                val params = bindingclass.skillsLeftPlayer1.layoutParams as LinearLayout.LayoutParams
                params.width = (displaywidth - fieldwidth) / 2
                bindingclass.skillsLeftPlayer1.layoutParams = params

                val params1 = bindingclass.skillsRight.layoutParams as LinearLayout.LayoutParams
                params1.width = (displaywidth - fieldwidth) / 2
                bindingclass.skillsRight.layoutParams=params1



                val params2 = bindingclass.field.layoutParams as LinearLayout.LayoutParams
                params2.width =  fieldwidth
                params2.height =  fieldheight
                bindingclass.field.layoutParams = params2
            }
            }




            sizeBaggage = blocksPool.height/2
            bindingclass.Player1.post {
                bindingclass.baggage.layoutParams.height = sizeBaggage
            }


            bindingclass.baggageBtn.setOnClickListener {
                    openFrag(Baggage_Fragment.newInstance(), bindingclass.baggage.id)
                    bindingclass.baggage.visibility = View.VISIBLE
                it.isEnabled = false

            }


        dataPlayMenu.LifeBaggage.observe(this,{
            bindingclass.baggage.visibility=View.GONE
            bindingclass.baggageBtn.isEnabled=true
        })


            //Тут вся логика взаимодействия поля и фигур
            root = bindingclass.rlGames
            rootblock = bindingclass.rlPool
            var centerY = displaymetrics.heightPixels / 2
            var centerX = displaymetrics.widthPixels / 2


            size = intent.getIntExtra("difficult", 0) * 10

            root.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        for (block in blocks.reversed())
                            if (block.containsPoint(event.x.toInt(), event.y.toInt())) {
                                downX = event.x.toInt()
                                downY = event.y.toInt()
                                lastX = downX
                                lastY = downY
                                capturedBlock = block
                                break
                            }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (capturedBlock != null) {
                            if (capturedBlock!!.detective) {
                                FieldPlayer1.figureOutDetection(capturedBlock!!)
                            }
                            capturedBlock!!.move(
                                event.x.toInt() - lastX,
                                event.y.toInt() - lastY
                            )
                            lastX = event.x.toInt()
                            lastY = event.y.toInt()
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        if (event.x.toInt() == downX && event.y.toInt() == downY) {
                            FieldPlayer1.figureOutDetection(capturedBlock!!)
                            capturedBlock?.rotate(FieldPlayer1)
                        } else {
                            if (capturedBlock != null) {
                                capturedBlock!!.detective =
                                    FieldPlayer1.figureDetection(capturedBlock!!)
                            }
                        }
                        capturedBlock = null
                    }
                }
                true
            }

        }
    private fun openFrag(f: Fragment, idHolder:Int){
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).add(idHolder,f).commit()
        if(f.isVisible()){
            supportFragmentManager.beginTransaction().replace(idHolder,f).commit()
        }else{

        }

    }
}



