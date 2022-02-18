package com.example.app3.basic_menu.train_game

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.app3.basic_menu.DataPlayMenu
import com.example.app3.constance.Constance
import com.example.app3.databinding.FragmentTrainGameBinding
import com.example.app3.games.Block
import com.example.app3.games.Field
import com.example.app3.games.Point
import com.example.app3.games.Pool


class Train_game : Fragment(){
    lateinit var binding: FragmentTrainGameBinding
    lateinit var Center : Point
    private lateinit var previewFields:Field
    val blocks = ArrayList<Block>()
    private val dataPlayMenu: DataPlayMenu by activityViewModels()
    private val hintsString= listOf("${Constance.HINT_1}",
        "${Constance.HINT_2}",
        "${Constance.HINT_3}",
        "${Constance.HINT_4}",
        "${Constance.HINT_5}",
        "You win!")
    private var numberHints=0
    private lateinit var pool: Pool
    var capturedBlock: Block? = null
    var lastX = 0
    var lastY = 0
    var downX = 0
    var downY = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTrainGameBinding.inflate(inflater)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
               binding.firstScreen.post {
                    binding.screen.visibility=View.INVISIBLE
                    sendHint(0,0,binding.firstScreen)
                }
               binding.lnFTrain.post {
                   Center = Point(binding.lnFTrain.width/2, binding.lnFTrain.height / 2)
                   previewFields = Field(binding.fieldTrain, 10, Center.x, Center.y, 50)
                   pool=Pool(binding.fieldTrain, blocks, 0, 0, 830)
                   binding.fieldTrain.post {
                       binding.lnFTrain.visibility = View.GONE
                   }
                   pool.actionI.observe(viewLifecycleOwner,{
                       if (it==2) {
                           sendHint(2, 0, binding.screen)
                       }
                       if (it==1) {
                           sendHint(3, 0, binding.screen)
                       }
                   })
               }

        var first_connect=true
        binding.fieldTrain.setOnTouchListener { v, event ->
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
                            previewFields.figureOutDetection(capturedBlock!!)
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
                        previewFields.figureOutDetection(capturedBlock!!)
                        capturedBlock?.rotate(previewFields)
                    }else{
                        if (capturedBlock != null) {
                            capturedBlock!!.detective= previewFields.figureDetection(capturedBlock!!)
                            if(capturedBlock!!.detective && first_connect){
                                sendHint(4, 1, binding.screen)
                                first_connect=false
                            }
                            if(previewFields.checkWin()){
                                sendHint(5, 2, binding.screen)
                            }
                        }
                    }
                    capturedBlock = null
                }
            }
            true
        }

        dataPlayMenu.activeHints.observe(activity as LifecycleOwner,{
            if(numberHints==1){
                sendHint(1, 1, binding.screen)
                binding.screen.visibility=View.VISIBLE
            }
                    binding.lnFTrain.visibility = View.VISIBLE
                    binding.screen.visibility=View.VISIBLE
            if(numberHints==6){
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }

                })
    }


    override fun onDestroy() {
        super.onDestroy()
        dataPlayMenu.activeMenu.value=true
    }


    fun sendHint(number:Int,RorL:Int,view:View){
        if(number==numberHints) {
            dataPlayMenu.HintString.value = hintsString[number]
            dataPlayMenu.RightLeft.value = RorL
            binding.lnFTrain.post {
            dataPlayMenu.TrainScrinshot.value = screenShot(view)
            numberHints++
                activity?.supportFragmentManager?.beginTransaction()?.replace(binding.hints.id, Hints.newInstance())?.commit()
                binding.hints.post {
                    binding.screen.visibility=View.GONE
                }
            }

        }
    }

    fun screenShot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    companion object {
        @JvmStatic
        fun newInstance() = Train_game()
    }

}