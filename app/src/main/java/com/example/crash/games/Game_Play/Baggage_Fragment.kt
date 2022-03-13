package com.example.crash.games.Game_Play

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.marginTop
import com.example.crash.R
import com.example.crash.databinding.FragmentBaggageBinding

class Baggage_Fragment : Fragment() {

    lateinit var baggageBinding: FragmentBaggageBinding
    var lastX = 0
    var lastY = 0
    var downX = 0
    var downY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baggageBinding= FragmentBaggageBinding.inflate(inflater)
        return baggageBinding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           // activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_in,R.anim.slide_out)?.remove(this)?.commit()

        baggageBinding.rlBaggage.setOnTouchListener { v, event ->
            when(event.action){

                MotionEvent.ACTION_DOWN->{
                    lastY = event.y.toInt()
                }


                MotionEvent.ACTION_MOVE->{
                    move(view,  event.y.toInt() - lastY)
                    lastY = event.y.toInt()
                    //activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_in,R.anim.slide_out)?.remove(this)?.commit()
                }

                MotionEvent.ACTION_UP->{
                    lastY = event.y.toInt()
                    close_Baggage(view.height/2,view)
                }

            }
            true
        }
    }

    fun move(relativeLayout: View, deltaY: Int) {
        val params = relativeLayout.layoutParams as FrameLayout.LayoutParams
        params.topMargin +=deltaY
        relativeLayout.layoutParams=params
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
        fun newInstance() = Baggage_Fragment()
    }
}