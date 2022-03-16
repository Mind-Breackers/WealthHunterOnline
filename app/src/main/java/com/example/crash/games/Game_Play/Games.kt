package com.example.crash.games.Game_Play

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
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
        val displayheight=displaymetrics.heightPixels
        val displaywidth=displaymetrics.widthPixels
        root=bindingclass.rlPool

        val blocksPool = Pool(root, blocks, 0, 0, displaywidth)
        bindingclass.rlPool.post {
            val fieldheight=(displayheight-bindingclass.rlPool.height)/2
            val fieldwidth=displaywidth-(bindingclass.skillsPayer2Left.width*2)
            //поле теперь динамически создается в зависимости от размера пула(надо как-то еще обдумать это будет
            bindingclass.field1.post {
                val FieldPlayer1 = Field(
                    bindingclass.rlField1,
                    30, blocksPool.cellSize,fieldheight,
                    fieldwidth
                )
            }

            //вычесление оставшегося размера для рюкзака
            bindingclass.field2.post {
                val FieldPlayer2 = Field(
                    bindingclass.rlField2,
                    30, blocksPool.cellSize,fieldheight,
                    fieldwidth
                )
            }
            sizeBaggage = bindingclass.rlPool.height/2
            bindingclass.rlField1.post {
                bindingclass.baggage.layoutParams.height = sizeBaggage
            }

        }
            bindingclass.btnBaggege.setOnClickListener {
                openFrag(Baggage_Fragment.newInstance(), bindingclass.baggage.id)
                bindingclass.baggage.visibility=View.VISIBLE
            }


        dataPlayMenu.LifeBaggage.observe(this,{
            bindingclass.baggage.visibility=View.GONE
        })




        //Тут вся логика взаимодействия поля и фигур
       /* root = bindingclass.root
        rootblock=bindingclass.rootblock
        var centerY = displaymetrics.heightPixels / 2
        var centerX = displaymetrics.widthPixels / 2
=======
        root = bindingclass.root
        rootblock = bindingclass.rootblock
        val centerY = displaymetrics.heightPixels / 2
        val centerX = displaymetrics.widthPixels / 2
>>>>>>> 7310fa8f41fdb432c77e4f583065f0b9df9906b4:app/src/main/java/com/example/app3/games/Games.kt

        size = intent.getIntExtra("difficult",0)*10

        val blocksPool = Pool(root, blocks, 0, 0, displaymetrics.widthPixels)
        val gamesField = Field(rootblock, size, centerX, centerY, blocksPool.cellSize)

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
                        if(capturedBlock!!.detective) {
                            gamesField.figureOutDetection(capturedBlock!!)
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
                        gamesField.figureOutDetection(capturedBlock!!)
                        capturedBlock?.rotate(gamesField)
                    }else{
                        if (capturedBlock != null) {
                          capturedBlock!!.detective= gamesField.figureDetection(capturedBlock!!)
                        }
                    }
                    capturedBlock = null
                }
            }
            true
        }*/

    }

    private fun openFrag(f: Fragment, idHolder:Int){
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).add(idHolder,f).commit()
        if(f.isVisible()){
            supportFragmentManager.beginTransaction().replace(idHolder,f).commit()
        }else{

        }

    }
}


