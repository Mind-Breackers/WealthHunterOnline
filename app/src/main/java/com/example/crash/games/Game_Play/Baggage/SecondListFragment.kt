package com.example.crash.games.Game_Play.Baggage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import com.example.crash.R
import com.example.crash.databinding.FragmentSecondListFragmentBinding
import com.example.crash.games.ClassForGame.Block
import com.example.crash.games.Game_Play.DataGames


class SecondListFragment : Fragment() {
    lateinit var baggageBinding:FragmentSecondListFragmentBinding
    private val dataGames: DataGames by activityViewModels()
    var lastX = 0
    var lastY = 0
    var downX = 0
    var sizeBaggage = 0
    var downY = 0
    private var blocks = arrayListOf<Block>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataGames.BlockInBaggage2.value?.let { blocks.addAll(it) }
        baggageBinding= FragmentSecondListFragmentBinding.inflate(inflater)
        return baggageBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baggageBinding.rlBaggageSecond.post {
            for (i in 0 until blocks.size) {
                val bl = Block(baggageBinding.rlBaggageSecond, baggageBinding.rlBaggageSecond.left, baggageBinding.rlBaggageSecond.top, 50, blocks[i])
                val colW = (baggageBinding.rlBaggageSecond.width * 0.33).toInt()
                blocks[i] = bl
                bl.move(
                    (colW * (i % 3) + (colW - bl.width) / 2),
                    (baggageBinding.rlBaggageSecond.height / 3 - bl.height / 3)
                )
                //bl.startCreateAnimation()

            }
        }

            baggageBinding.baggageSecond.setOnTouchListener { v, event ->
                when (event.action) {

                    MotionEvent.ACTION_DOWN -> {
                        if (blocks.size != 0) {
                            for (block in blocks.reversed()) {
                                if (block!!.containsPoint(event.x.toInt(), event.y.toInt())) {
                                    val captureBlock=block
                                    block.destroy(blocks)
                                    dataGames.BlockInBaggage2.value = blocks
                                    dataGames.BlockOutBaggage.value = captureBlock
                                    activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_in,R.anim.slide_out)?.remove(this)?.commit()
                                    break
                                }
                            }
                            lastY = event.y.toInt()
                            lastX=event.x.toInt()
                        } else {
                            lastY = event.y.toInt()
                            lastX=event.x.toInt()
                        }
                    }


                    MotionEvent.ACTION_MOVE -> {
                        if(event.x.toInt()-lastX<35&&lastX-event.x.toInt()<35) {
                            move(baggageBinding.rlBaggageSecond, event.y.toInt() - lastY)
                            lastY = event.y.toInt()
                            check_in_baggage(lastY, view)
                        } else {
                            close_Baggage(view.height / 2, baggageBinding.rlBaggageSecond)
                            lastY = event.y.toInt()
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        check_in_baggage(lastY, view)
                        lastY = event.y.toInt()
                        close_Baggage(view.height / 2, baggageBinding.rlBaggageSecond)
                    }


                }
                true
            }


    }


    override fun onResume() {
        move(baggageBinding.rlBaggageSecond,0-baggageBinding.rlBaggageSecond.marginTop)
        super.onResume()
    }


    override fun onPause() {
        move(baggageBinding.rlBaggageSecond,0-baggageBinding.rlBaggageSecond.marginTop)
        super.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        baggageBinding.baggageSecond.removeAllViews()
        dataGames.LifeBaggage.value=true
    }



    fun move(relativeLayout: View, deltaY: Int) {
        if(relativeLayout.marginTop+deltaY>=0) {
            val params = relativeLayout.layoutParams as FrameLayout.LayoutParams
            params.topMargin += deltaY
            relativeLayout.layoutParams = params
        }
    }


    fun check_in_baggage(y:Int,view: View){
        if(y<0){
            move(view,0-view.marginTop)
        }
        if(y>view.height){
             activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_in,R.anim.slide_out)?.remove(this)?.commit()
        }
    }

    fun close_Baggage(centerView:Int,view: View){
        if(view.marginTop>centerView){
             activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_in,R.anim.slide_out)?.remove(this)?.commit()
        }
        else{
            move(view,0-view.marginTop)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SecondListFragment()
    }
}