package com.example.app3.games

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.app3.databinding.GamesBinding


class Games : AppCompatActivity() {
    lateinit var bindingclass: GamesBinding
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
        bindingclass = GamesBinding.inflate(layoutInflater)
        setContentView(bindingclass.root)

        val displaymetrics = resources.displayMetrics



        root = bindingclass.root
        rootblock=bindingclass.rootblock
        var centerY = displaymetrics.heightPixels / 2
        var centerX = displaymetrics.widthPixels / 2

        size=intent.getIntExtra("difficult",0)*10

        val GamesField=Field(rootblock,size, centerX-25, centerY-25)
        Pool(root, blocks, 0, 0, 830)

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
                            GamesField.figureOutDetection(capturedBlock!!)
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
                        GamesField.figureOutDetection(capturedBlock!!)
                        capturedBlock?.rotate(GamesField)
                    }else{
                        if (capturedBlock != null) {
                          capturedBlock!!.detective= GamesField.figureDetection(capturedBlock!!)
                        }
                    }
                    capturedBlock = null
                }
            }
            true
        }

    }
}

