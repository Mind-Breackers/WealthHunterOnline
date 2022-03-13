package com.example.crash.games.Game_Play

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.crash.R
import com.example.crash.databinding.Games2Binding
import com.example.crash.games.Block
import com.example.crash.games.Field
import com.example.crash.games.Pool


class Games : AppCompatActivity() {
    lateinit var bindingclass: Games2Binding
    lateinit var root: RelativeLayout
    lateinit var rootblock: RelativeLayout
    val blocks = ArrayList<Block>()
    var capturedBlock: Block? = null
    var lastX = 0
    var lastY = 0
    var downX = 0
    var downY = 0
    private var size=0

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingclass = Games2Binding.inflate(layoutInflater)
        setContentView(bindingclass.root)

        val displaymetrics = resources.displayMetrics


        root=bindingclass.rlPool

        bindingclass.rlPool.post {
            val blocksPool = Pool(root, blocks, 0, 0, bindingclass.rlPool.width)

            bindingclass.field1.post {
                val FieldPlayer1 = Field(
                    bindingclass.rlField1,
                    30,
                    bindingclass.field1.width / 2,
                    bindingclass.field1.height / 2, blocksPool.cellSize

                )
            }
            bindingclass.field2.post {
                val FieldPlayer2 = Field(
                    bindingclass.rlField2,
                    30,
                    bindingclass.field2.width / 2,
                    bindingclass.field2.height / 2, blocksPool.cellSize
                )
            }
        }
            bindingclass.btnBaggege.setOnClickListener {
                openFrag(Baggage_Fragment.newInstance(), bindingclass.baggage.id)

            }

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


